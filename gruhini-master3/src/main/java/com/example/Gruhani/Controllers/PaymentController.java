package com.example.Gruhani.Controllers;

import com.razorpay.RazorpayClient;
import com.razorpay.Payment;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Value("${razorpay.key.id:rzp_test_xxxxxxxx}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret:xxxxxxxx}")
    private String razorpayKeySecret;

    private static final String HEX = "0123456789abcdef";

    /**
     * POST /payments/razorpay/verify
     * Verify Razorpay payment signature
     */
    @PostMapping("/razorpay/verify")
    public ResponseEntity<?> verifyRazorpayPayment(@RequestBody Map<String, String> payload) {
        try {
            String razorpayOrderId = payload.get("razorpayOrderId");
            String razorpayPaymentId = payload.get("razorpayPaymentId");
            String razorpaySignature = payload.get("razorpaySignature");
            String orderId = payload.get("orderId");

            // Verify signature
            String verificationMessage = razorpayOrderId + "|" + razorpayPaymentId;
            String expectedSignature = generateSignature(verificationMessage, razorpayKeySecret);

            if (!expectedSignature.equals(razorpaySignature)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Invalid payment signature");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Fetch payment details from Razorpay
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            Payment payment = client.Payments.fetch(razorpayPaymentId);

            // Check payment status
            if (payment.get("status").equals("captured")) {
                // Payment successful - update order status to CONFIRMED
                Map<String, Object> successResponse = new HashMap<>();
                successResponse.put("success", true);
                successResponse.put("orderId", orderId);
                successResponse.put("paymentStatus", "COMPLETED");
                successResponse.put("razorpayPaymentId", razorpayPaymentId);
                successResponse.put("message", "Payment verified successfully ✅");
                
                return ResponseEntity.ok(successResponse);
            } else {
                Map<String, Object> pendingResponse = new HashMap<>();
                pendingResponse.put("success", false);
                pendingResponse.put("message", "Payment not captured yet");
                return ResponseEntity.badRequest().body(pendingResponse);
            }

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Payment verification failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * POST /payments/razorpay/webhook
     * Razorpay webhook for payment updates
     */
    @PostMapping("/razorpay/webhook")
    public ResponseEntity<?> handleRazorpayWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            // Verify webhook signature
            String expectedSignature = generateSignature(payload, razorpayKeySecret);
            
            if (!expectedSignature.equals(signature)) {
                return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                    put("error", "Invalid webhook signature");
                }});
            }

            JSONObject event = new JSONObject(payload);
            String eventType = event.getString("event");

            // Handle different payment events
            if ("payment.authorized".equals(eventType)) {
                JSONObject payment = event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                handlePaymentAuthorized(payment);
            } else if ("payment.failed".equals(eventType)) {
                JSONObject payment = event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                handlePaymentFailed(payment);
            } else if ("payment.captured".equals(eventType)) {
                JSONObject payment = event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                handlePaymentCaptured(payment);
            }

            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("status", "received");
            }});

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Webhook processing failed: " + e.getMessage());
            }});
        }
    }

    /**
     * POST /payments/razorpay/create-order
     * Create Razorpay order
     */
    @PostMapping("/razorpay/create-order")
    public ResponseEntity<?> createRazorpayOrder(@RequestBody Map<String, Object> payload) {
        try {
            Long amount = ((Number) payload.get("amount")).longValue(); // in paise
            String currency = (String) payload.get("currency");
            String receipt = (String) payload.get("receipt");

            // Create order via Razorpay API
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", receipt);

            com.razorpay.Order order = client.Orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("razorpayOrderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("status", order.get("status"));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", "Order creation failed: " + e.getMessage());
            }});
        }
    }

    // ===== PRIVATE HELPER METHODS =====

    private void handlePaymentAuthorized(JSONObject payment) {
        String orderId = payment.optString("description");
        String paymentId = payment.optString("id");
        System.out.println("✅ Payment Authorized - Order: " + orderId + ", Payment: " + paymentId);
        // TODO: Update order status to AUTHORIZED
    }

    private void handlePaymentFailed(JSONObject payment) {
        String orderId = payment.optString("description");
        String error = payment.optString("error_description");
        System.out.println("❌ Payment Failed - Order: " + orderId + ", Error: " + error);
        // TODO: Update order status to PAYMENT_FAILED, offer retry
    }

    private void handlePaymentCaptured(JSONObject payment) {
        String orderId = payment.optString("description");
        String paymentId = payment.optString("id");
        long amount = payment.optLong("amount");
        System.out.println("✅ Payment Captured - Order: " + orderId + ", Amount: " + amount);
        // TODO: Update order status to CONFIRMED, send notifications
    }

    private String generateSignature(String message, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        
        byte[] hash = sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
        
        // Convert to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(HEX.charAt((b & 0xF0) >> 4));
            hexString.append(HEX.charAt(b & 0x0F));
        }
        
        return hexString.toString();
    }
}
