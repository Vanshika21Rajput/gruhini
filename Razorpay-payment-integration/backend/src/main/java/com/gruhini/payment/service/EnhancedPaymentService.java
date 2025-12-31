package com.gruhini.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruhini.payment.dto.*;
import com.gruhini.payment.exception.*;
import com.gruhini.payment.model.*;
import com.gruhini.payment.repository.*;
import com.gruhini.payment.service.FraudDetectionService.FraudScore;
import com.gruhini.payment.service.FraudDetectionService.RiskLevel;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🔒 ENTERPRISE-GRADE PAYMENT SERVICE
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EnhancedPaymentService {
    
    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final AuditLogRepository auditLogRepository;
    private final FraudDetectionService fraudDetectionService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Helper for JSON conversion
    
    @Value("${razorpay.key-secret}")
    private String keySecret;
    
    // In-memory cache for idempotency (use Redis in production)
    private final Map<String, String> idempotencyCache = new ConcurrentHashMap<>();
    
    // Rate limiting (requests per IP per hour)
    private final Map<String, List<LocalDateTime>> rateLimitMap = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_HOUR = 50;
    
    /**
     * 🎯 STEP 1: CREATE ORDER WITH COMPREHENSIVE VALIDATION
     */
    @Transactional
    public PaymentResponse createOrder(OrderRequest request, String ipAddress, String idempotencyKey) {
        
        // LAYER 1: IDEMPOTENCY CHECK
        if (idempotencyKey != null && idempotencyCache.containsKey(idempotencyKey)) {
            log.warn("⚠️ Duplicate request detected: {}", idempotencyKey);
            String existingOrderId = idempotencyCache.get(idempotencyKey);
            return getExistingOrderResponse(existingOrderId);
        }
        
        // LAYER 2: RATE LIMITING
        if (!checkRateLimit(ipAddress)) {
            log.error("🚫 Rate limit exceeded for IP: {}", ipAddress);
            auditLog("RATE_LIMIT_EXCEEDED", ipAddress, request);
            throw new RateLimitException("Too many payment attempts. Please try after 1 hour.");
        }
        
        // LAYER 3: FRAUD DETECTION
        FraudScore fraudScore = fraudDetectionService.analyze(request, ipAddress);
        
        if (fraudScore.getRiskLevel() == RiskLevel.HIGH) {
            log.error("🚨 High fraud risk detected! Score: {}", fraudScore.getScore());
            auditLog("FRAUD_DETECTED", ipAddress, request, fraudScore);
            throw new FraudException("Payment blocked due to suspicious activity.");
        }
        
        // LAYER 4: AMOUNT VALIDATION & CART INTEGRATION
        BigDecimal finalAmount;
        List<OrderRequest.CartItem> finalItems;
        Long transactionCartId = request.getCartId();

        if (transactionCartId != null) {
            // SECURE FLOW: Fetch from DB
            Cart cart = cartRepository.findById(transactionCartId)
                    .orElseThrow(() -> new PaymentException("Invalid Cart ID provided"));
            
            BigDecimal cartTotal = cart.getTotalAmount();
            BigDecimal deliveryCharge = new BigDecimal("33.60"); // Fixed for this MVP
            finalAmount = cartTotal.add(deliveryCharge);
            
            // Log if there's a discrepancy/hacking attempt, but proceed with REAL amount
            if (request.getAmount() != null && finalAmount.compareTo(request.getAmount()) != 0) {
                 log.warn("🚨 PRICE MANIPULATION ATTEMPT? Frontend: {}, Backend: {}. Using Backend Amount.", request.getAmount(), finalAmount);
            }
            
            // Map Cart Items to Order Items
            finalItems = cart.getItems().stream().map(item -> {
                OrderRequest.CartItem reqItem = new OrderRequest.CartItem();
                reqItem.setName(item.getProductName());
                reqItem.setPrice(item.getPrice());
                reqItem.setQuantity(item.getQuantity());
                return reqItem;
            }).toList();
            
        } else {
            // FALLBACK / LEGACY FLOW (Should be blocked in PROD)
            log.warn("⚠️ Creating order WITHOUT Cart ID. Using trusted frontend data (NOT RECOMMENDED).");
            validateAmount(request.getAmount());
            finalAmount = request.getAmount();
            finalItems = request.getItems();
            
            BigDecimal calculatedAmount = finalItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
            if (calculatedAmount.compareTo(finalAmount) != 0) {
                 throw new AmountMismatchException("Order amount doesn't match items total.");
            }
        }
        
        // LAYER 5: CREATE ORDER IN DATABASE
        com.gruhini.payment.model.Order dbOrder = com.gruhini.payment.model.Order.builder()
                .id(UUID.randomUUID().toString())
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerPhone(sanitizePhone(request.getCustomerPhone()))
                .amount(finalAmount) // Using SECURE amount
                .status(OrderStatus.PENDING)
                .items(convertItemsToJson(finalItems))
                .fraudScore(fraudScore.getScore())
                .riskLevel(fraudScore.getRiskLevel().toString())
                .ipAddress(ipAddress)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        
        dbOrder = orderRepository.save(dbOrder);
        log.info("✅ Order created in DB: {} with fraud score: {}", dbOrder.getId(), fraudScore.getScore());
        
        // LAYER 6: CREATE RAZORPAY ORDER
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", finalAmount.multiply(BigDecimal.valueOf(100)).intValue());
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", dbOrder.getId());
            orderRequest.put("payment_capture", 1);
            
            JSONObject notes = new JSONObject();
            notes.put("order_id", dbOrder.getId());
            notes.put("customer_email", request.getCustomerEmail());
            notes.put("fraud_score", fraudScore.getScore());
            orderRequest.put("notes", notes);
            
            Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            String razorpayOrderId = razorpayOrder.get("id");
            
            // LAYER 7: CREATE PAYMENT RECORD
            com.gruhini.payment.model.Payment payment = com.gruhini.payment.model.Payment.builder()
                    .id(UUID.randomUUID().toString())
                    .order(dbOrder)
                    .razorpayOrderId(razorpayOrderId)
                    .amount(finalAmount)
                    .status(PaymentStatus.CREATED)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            paymentRepository.save(payment);
            
            // LAYER 8: STORE IDEMPOTENCY KEY
            if (idempotencyKey != null) {
                idempotencyCache.put(idempotencyKey, dbOrder.getId());
            }
            
            // LAYER 9: AUDIT LOG
            auditLog("ORDER_CREATED", ipAddress, "Order Created: " + dbOrder.getId());
            
            // LAYER 10: RETURN RESPONSE
            return PaymentResponse.builder()
                    .orderId(dbOrder.getId())
                    .razorpayOrderId(razorpayOrderId)
                    .amount(finalAmount)
                    .currency("INR")
                    .keyId(razorpayClient.get("key_id"))
                    .expiresAt(dbOrder.getExpiresAt())
                    .fraudScore(fraudScore.getScore())
                    .riskLevel(fraudScore.getRiskLevel().toString())
                    .build();
                    
        } catch (Exception e) {
            log.error("❌ Razorpay order creation failed", e);
            dbOrder.setStatus(OrderStatus.FAILED);
            orderRepository.save(dbOrder);
            auditLog("ORDER_CREATION_FAILED", ipAddress, "Failed for Order " + dbOrder.getId() + ": " + e.getMessage());
            throw new PaymentException("Failed to create payment order: " + e.getMessage());
        }
    }
    
    /**
     * 🔐 STEP 2: VERIFY PAYMENT WITH MULTI-LAYER VALIDATION
     */
    @Transactional
    public PaymentVerificationResponse verifyPayment(PaymentVerificationRequest request, String ipAddress) {
        
        log.info("🔍 Starting payment verification for order: {}", request.getRazorpayOrderId());
        
        // CHECK 1: FIND PAYMENT RECORD
        com.gruhini.payment.model.Payment payment = paymentRepository
                .findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new PaymentNotFoundException("Payment record not found"));
        
        com.gruhini.payment.model.Order order = payment.getOrder();
        
        // CHECK 2: VERIFY ORDER IS NOT EXPIRED
        if (order.getExpiresAt() != null && LocalDateTime.now().isAfter(order.getExpiresAt())) {
            log.error("⏰ Order expired: {}", order.getId());
            auditLog("ORDER_EXPIRED", ipAddress, "Expired Order: " + order.getId());
            
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.EXPIRED);
            
            paymentRepository.save(payment);
            orderRepository.save(order);
            
            return PaymentVerificationResponse.builder()
                    .success(false)
                    .message("Order has expired. Please create a new order.")
                    .errorCode("ORDER_EXPIRED")
                    .build();
        }
        
        // CHECK 3: VERIFY PAYMENT NOT ALREADY PROCESSED
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            log.warn("⚠️ Payment already verified: {}", payment.getId());
            return PaymentVerificationResponse.builder()
                    .success(true)
                    .message("Payment already verified")
                    .orderId(order.getId())
                    .build();
        }
        
        // CHECK 4: VERIFY RAZORPAY SIGNATURE
        String calculatedSignature = calculateHMAC(request.getRazorpayOrderId(), request.getRazorpayPaymentId());
        if (!calculatedSignature.equals(request.getRazorpaySignature())) {
            log.error("🚨 SIGNATURE MISMATCH!");
            auditLog("SIGNATURE_MISMATCH", ipAddress, "Signature mismatch for Payment: " + payment.getId());
            
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            notificationService.sendSecurityAlert("Payment signature mismatch", order.getId());
            
            return PaymentVerificationResponse.builder()
                    .success(false)
                    .message("Payment verification failed")
                    .errorCode("SIGNATURE_INVALID")
                    .build();
        }
        
        // CHECK 5: VERIFY WITH RAZORPAY API
        try {
            Payment razorpayPayment = razorpayClient.payments.fetch(request.getRazorpayPaymentId());
            String razorpayStatus = razorpayPayment.get("status");
            Integer razorpayAmount = razorpayPayment.get("amount");
            String razorpayOrderId = razorpayPayment.get("order_id");
            
            if (!"captured".equals(razorpayStatus) && !"authorized".equals(razorpayStatus)) {
                log.error("❌ Payment status is: {}", razorpayStatus);
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                return PaymentVerificationResponse.builder()
                        .success(false)
                        .message("Payment not completed: " + razorpayStatus)
                        .errorCode("PAYMENT_NOT_CAPTURED")
                        .build();
            }
            
            BigDecimal razorpayAmountDecimal = BigDecimal.valueOf(razorpayAmount).divide(BigDecimal.valueOf(100));
            if (razorpayAmountDecimal.compareTo(order.getAmount()) != 0) {
                log.error("💰 Amount mismatch! Expected: {}, Got: {}", order.getAmount(), razorpayAmountDecimal);
                auditLog("AMOUNT_MISMATCH", ipAddress, "Amount mismatch. Expected: " + order.getAmount() + ", Got: " + razorpayAmountDecimal);
                return PaymentVerificationResponse.builder()
                        .success(false)
                        .message("Amount verification failed")
                        .errorCode("AMOUNT_MISMATCH")
                        .build();
            }
            
            if (!razorpayOrderId.equals(request.getRazorpayOrderId())) {
                log.error("🔢 Order ID mismatch!");
                return PaymentVerificationResponse.builder()
                        .success(false)
                        .message("Order ID mismatch")
                        .errorCode("ORDER_ID_MISMATCH")
                        .build();
            }
            
            // ALL CHECKS PASSED
            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpaySignature(request.getRazorpaySignature());
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            
            String method = razorpayPayment.get("method");
            try {
                payment.setMethod(PaymentMethod.valueOf(method.toUpperCase()));
            } catch (Exception e) {
                // Default or unknown method handling
                log.warn("Unknown payment method: {}", method);
            }
            
            if ("upi".equals(method)) {
                JSONObject upi = razorpayPayment.has("upi") ? razorpayPayment.getJSONObject("upi") : null;
                if (upi != null) payment.setUpiId(upi.optString("vpa", null));
            } else if ("card".equals(method)) {
                 JSONObject card = razorpayPayment.has("card") ?  razorpayPayment.getJSONObject("card") : null;
                 if (card != null) payment.setCardLast4(card.optString("last4", null));
            }
            
            order.setStatus(OrderStatus.CONFIRMED);
            order.setUpdatedAt(LocalDateTime.now());
            
            paymentRepository.save(payment);
            orderRepository.save(order);
            
            log.info("✅ Payment verified successfully: {}", payment.getId());
            auditLog("PAYMENT_SUCCESS", ipAddress, "Payment Success: " + payment.getId());
            notificationService.sendPaymentConfirmation(order, payment);
            
            return PaymentVerificationResponse.builder()
                    .success(true)
                    .message("Payment verified successfully")
                    .orderId(order.getId())
                    .paymentId(payment.getId())
                    .amount(order.getAmount())
                    .build();
                    
        } catch (Exception e) {
            log.error("❌ Razorpay API verification failed", e);
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            auditLog("VERIFICATION_ERROR", ipAddress, "Verification error: " + e.getMessage());
            return PaymentVerificationResponse.builder()
                    .success(false)
                    .message("Verification failed: " + e.getMessage())
                    .errorCode("VERIFICATION_ERROR")
                    .build();
        }
    }
    
    public PaymentStatusResponse getPaymentStatus(String orderId, String ipAddress) {
        com.gruhini.payment.model.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
        
        com.gruhini.payment.model.Payment payment = order.getPayment();
        
        if (payment == null) {
             return PaymentStatusResponse.builder()
                    .orderId(orderId)
                    .status("PENDING")
                    .message("Payment not initiated")
                    .build();
        }
        
        if (order.getExpiresAt() != null && LocalDateTime.now().isAfter(order.getExpiresAt())) {
            if (order.getStatus() != OrderStatus.EXPIRED) {
                order.setStatus(OrderStatus.EXPIRED);
                orderRepository.save(order);
            }
            return PaymentStatusResponse.builder()
                    .orderId(orderId)
                    .status("EXPIRED")
                    .message("Order has expired")
                    .timeRemaining(0L)
                    .build();
        }
        
        Long timeRemaining = order.getExpiresAt() != null ? 
            java.time.Duration.between(LocalDateTime.now(), order.getExpiresAt()).getSeconds() : null;
            
        return PaymentStatusResponse.builder()
                .orderId(orderId)
                .paymentId(payment.getId())
                .status(payment.getStatus().toString())
                .message(getStatusMessage(payment.getStatus()))
                .amount(order.getAmount())
                .timeRemaining(timeRemaining)
                .fraudScore(order.getFraudScore())
                .build();
    }
    
    // Stub for Webhook processing (User's Controller called this)
    public void processWebhook(String payload, String signature) {
        // Implement webhook processing logic (signature verification etc.)
        // This is a placeholder to satisfy the controller hook
        log.info("Processing webhook payload");
    }

    public String getRazorpayKeyId() {
        return razorpayClient.has("key_id") ? razorpayClient.get("key_id").toString() : "rzp_test_placeholder";
    }

    // HELPER METHODS
    private String calculateHMAC(String orderId, String paymentId) {
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(payload.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new PaymentException("Signature calculation failed");
        }
    }
    
    private boolean checkRateLimit(String ipAddress) {
        rateLimitMap.putIfAbsent(ipAddress, new ArrayList<>());
        List<LocalDateTime> requests = rateLimitMap.get(ipAddress);
        requests.removeIf(time -> time.isBefore(LocalDateTime.now().minusHours(1)));
        if (requests.size() >= MAX_REQUESTS_PER_HOUR) return false;
        requests.add(LocalDateTime.now());
        return true;
    }
    
    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(1)) < 0) throw new InvalidAmountException("Minimum amount is ₹1");
        if (amount.compareTo(BigDecimal.valueOf(100000)) > 0) throw new InvalidAmountException("Maximum amount is ₹1,00,000");
    }
    
    private String sanitizePhone(String phone) {
        return phone.replaceAll("[^0-9]", "");
    }
    
    private void auditLog(String event, String ipAddress, Object data) {
        AuditLog log = new AuditLog();
        log.setEvent(event);
        log.setIpAddress(ipAddress);
        try {
            log.setData(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.setData(data.toString());
        }
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
    
    private String getStatusMessage(PaymentStatus status) {
        switch (status) {
            case CREATED: return "Payment initiated. Awaiting user action.";
            case PENDING: return "Payment in progress...";
            case PROCESSING: return "Verifying payment...";
            case SUCCESS: return "Payment successful!";
            case FAILED: return "Payment failed. Please try again.";
            case REFUNDED: return "Payment refunded.";
            default: return "Unknown status";
        }
    }
    
    private PaymentResponse getExistingOrderResponse(String orderId) {
        com.gruhini.payment.model.Order order = orderRepository.findById(orderId).orElseThrow();
        String key = "rzp_test_placeholder";
        try {
             if(razorpayClient.has("key_id")) key = razorpayClient.get("key_id");
        } catch(Exception e) {}
        
        return PaymentResponse.builder()
                .orderId(order.getId())
                .razorpayOrderId(order.getPayment() != null ? order.getPayment().getRazorpayOrderId() : null)
                .amount(order.getAmount())
                .currency("INR")
                .keyId(key)
                .expiresAt(order.getExpiresAt())
                .build();
    }
    
    private String convertItemsToJson(List<OrderRequest.CartItem> items) {
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}
