package com.gruhini.payment.controller;

import com.gruhini.payment.dto.*;
import com.gruhini.payment.exception.*;
import com.gruhini.payment.service.EnhancedPaymentService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🚀 PRODUCTION-GRADE PAYMENT CONTROLLER
 * 
 * Security Features:
 * ✅ Rate limiting (per IP) via Bucket4j
 * ✅ Idempotency keys
 * ✅ Request validation
 * ✅ IP tracking
 * ✅ Comprehensive error handling
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Use specific origins in production
public class PaymentController {
    
    private final EnhancedPaymentService paymentService;
    
    // Rate limiting: 10 requests per minute per IP
    private final Map<String, Bucket> rateLimitBuckets = new ConcurrentHashMap<>();
    
    @PostMapping("/create-order")
    public ResponseEntity<ApiResponse<PaymentResponse>> createOrder(
            @Valid @RequestBody OrderRequest request,
            HttpServletRequest httpRequest,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey) {
        
        String ipAddress = getClientIP(httpRequest);
        log.info("📝 Order creation request from IP: {}", ipAddress);
        
        if (!checkRateLimit(ipAddress)) {
            log.warn("⚠️ Rate limit exceeded for IP: {}", ipAddress);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ApiResponse.error("Rate limit exceeded. Please try after 1 minute.", "RATE_LIMIT_EXCEEDED"));
        }
        
        if (idempotencyKey == null) {
            idempotencyKey = UUID.randomUUID().toString();
        }
        
        try {
            PaymentResponse response = paymentService.createOrder(request, ipAddress, idempotencyKey);
            return ResponseEntity.ok(ApiResponse.success(response, "Order created successfully"));
            
        } catch (RateLimitException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ApiResponse.error(e.getMessage(), "RATE_LIMIT"));
        } catch (FraudException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage(), "FRAUD_DETECTED"));
        } catch (AmountMismatchException | InvalidAmountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage(), "INVALID_AMOUNT"));
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Failed to create payment order.", "PAYMENT_CREATION_FAILED"));
        } catch (Exception e) {
            log.error("💥 Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("An unexpected error occurred.", "INTERNAL_ERROR"));
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<PaymentVerificationResponse>> verifyPayment(
            @Valid @RequestBody PaymentVerificationRequest request,
            HttpServletRequest httpRequest) {
        
        String ipAddress = getClientIP(httpRequest);
        log.info("🔍 Payment verification request for order: {}", request.getRazorpayOrderId());
        
        if (!checkRateLimit(ipAddress)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ApiResponse.error("Too many verification attempts.", "RATE_LIMIT_EXCEEDED"));
        }
        
        try {
            PaymentVerificationResponse response = paymentService.verifyPayment(request, ipAddress);
            if (response.isSuccess()) {
                return ResponseEntity.ok(ApiResponse.success(response, "Payment verified successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(response.getMessage(), response.getErrorCode()));
            }
        } catch (PaymentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage(), "PAYMENT_NOT_FOUND"));
        } catch (Exception e) {
            log.error("💥 Error verification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Verification failed.", "VERIFICATION_ERROR"));
        }
    }
    
    @GetMapping("/status/{orderId}")
    public ResponseEntity<ApiResponse<PaymentStatusResponse>> getPaymentStatus(
            @PathVariable String orderId,
            HttpServletRequest httpRequest) {
        
        String ipAddress = getClientIP(httpRequest);
        try {
            PaymentStatusResponse status = paymentService.getPaymentStatus(orderId, ipAddress);
            return ResponseEntity.ok(ApiResponse.success(status, "Status retrieved successfully"));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Order not found: " + orderId, "ORDER_NOT_FOUND"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Failed to retrieve status", "STATUS_ERROR"));
        }
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature,
            HttpServletRequest httpRequest) {
        
        String ipAddress = getClientIP(httpRequest);
        log.info("🔔 Webhook received from IP: {}", ipAddress);
        
        try {
            paymentService.processWebhook(payload, signature);
            return ResponseEntity.ok("Webhook processed");
        } catch (Exception e) {
            log.error("❌ Webhook processing failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook processing failed");
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "payment-service",
                "timestamp", System.currentTimeMillis(),
                "version", "2.0.0"
        ));
    }
    
    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        return ResponseEntity.ok(Map.of(
                "keyId", paymentService.getRazorpayKeyId(),
                "currency", "INR",
                "timeout", "900"
        ));
    }

    // HELPER METHODS
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
    
    private boolean checkRateLimit(String ipAddress) {
        Bucket bucket = rateLimitBuckets.computeIfAbsent(ipAddress, k -> {
            Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
            return Bucket.builder().addLimit(limit).build();
        });
        return bucket.tryConsume(1);
    }
}
