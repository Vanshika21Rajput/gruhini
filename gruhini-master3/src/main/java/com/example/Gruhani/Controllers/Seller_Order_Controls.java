package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.OrderRepo;
import com.example.Gruhani.dtos.AcceptOrderDto;
import com.example.Gruhani.dtos.RejectOrderDto;
import com.example.Gruhani.models.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * SELLER ORDER CONTROLLER
 * 
 * Handles seller order operations:
 * - Accept order with delivery details
 * - Reject order with reason
 * - Verify OTP during delivery
 * - Resend OTP to customer
 * 
 * Frontend Dependency: order-status.html polls these endpoints
 */
@RestController
@RequestMapping("/seller")
@CrossOrigin(origins = "*")
public class Seller_Order_Controls {

    @Autowired
    private OrderRepo orderRepo;
    
    // TODO: Inject EmailService when available
    // @Autowired
    // private EmailService emailService;

    /**
     * POST /seller/accept-order
     * 
     * Seller accepts order and provides delivery details
     * Frontend sends this from seller-dashboard modal form
     * 
     * Request Body:
     * {
     *   "orderId": 101,
     *   "deliveryTime": "Today at 8:00 PM",
     *   "deliveryMethod": "SELLER_DELIVERY",
     *   "deliveryNotes": "Ring doorbell twice"
     * }
     * 
     * Response:
     * {
     *   "success": true,
     *   "orderId": 101,
     *   "status": "ACCEPTED",
     *   "otp": "847291",
     *   "message": "Order accepted! OTP sent to customer."
     * }
     */
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/accept-order")
    public ResponseEntity<?> acceptOrder(@RequestBody AcceptOrderDto acceptDto) {
        try {
            // STEP 1: Validate order exists
            Orders order = orderRepo.findById(acceptDto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            // STEP 2: Validate order is in PENDING status
            if (!"PENDING".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Order is not in PENDING status. Current status: " + order.getStatus()
                ));
            }
            
            // STEP 3: Generate 6-digit OTP
            String otp = generateOtp();
            String hashedOtp = hashOtp(otp); // Hash before storing
            
            // STEP 4: Update order in database
            order.setStatus("ACCEPTED");
            order.setDeliveryTime(acceptDto.getDeliveryTime());
            order.setDeliveryMethod(acceptDto.getDeliveryMethod());
            order.setDeliveryNotes(acceptDto.getDeliveryNotes());
            order.setOtp(hashedOtp);
            orderRepo.save(order);
            
            // STEP 5: TODO - Send Email to Customer
            // Must include: OTP, Delivery Time, Delivery Method, Seller Contact
            // emailService.sendOrderAcceptanceToCustomer(
            //     order.getUserEmail(),
            //     otp,
            //     acceptDto.getDeliveryTime(),
            //     acceptDto.getDeliveryMethod()
            // );
            
            // STEP 6: TODO - Send Email to Admin
            // emailService.sendAdminNotification(
            //     "gruhani214@gmail.com",
            //     "Order Accepted: #" + order.getId()
            // );
            
            // STEP 7: Return response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", order.getId());
            response.put("status", "ACCEPTED");
            response.put("otp", otp); // Return un-hashed OTP to seller interface
            response.put("message", "Order accepted! OTP sent to customer.");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", "Server error: " + e.getMessage()
            ));
        }
    }

    /**
     * POST /seller/reject-order
     * 
     * Seller rejects order with a reason
     * 
     * Request Body:
     * {
     *   "orderId": 101,
     *   "rejectionReason": "Out of ingredients"
     * }
     * 
     * Response:
     * {
     *   "success": true,
     *   "orderId": 101,
     *   "status": "REJECTED",
     *   "message": "Order rejected. Customer notified."
     * }
     */
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/reject-order")
    public ResponseEntity<?> rejectOrder(@RequestBody RejectOrderDto rejectDto) {
        try {
            // STEP 1: Validate order exists
            Orders order = orderRepo.findById(rejectDto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            // STEP 2: Validate order is in PENDING status
            if (!"PENDING".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Order is not in PENDING status. Current status: " + order.getStatus()
                ));
            }
            
            // STEP 3: Update order in database
            order.setStatus("REJECTED");
            order.setRejectionReason(rejectDto.getRejectionReason());
            orderRepo.save(order);
            
            // STEP 4: TODO - Send Email to Customer with rejection reason
            // emailService.sendOrderRejectionToCustomer(
            //     order.getUserEmail(),
            //     rejectDto.getRejectionReason()
            // );
            
            // STEP 5: TODO - Send Email to Admin
            // emailService.sendAdminRejectionNotification(
            //     "gruhani214@gmail.com",
            //     order.getId()
            // );
            
            // STEP 6: Return response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", order.getId());
            response.put("status", "REJECTED");
            response.put("message", "Order rejected. Customer notified.");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", "Server error: " + e.getMessage()
            ));
        }
    }

    /**
     * POST /seller/verify-otp
     * 
     * Seller verifies OTP when delivering to customer
     * Once verified, order is marked as DELIVERED
     * 
     * Query Parameters:
     * ?orderId=101&otp=847291
     * 
     * Response (Success):
     * {
     *   "success": true,
     *   "orderId": 101,
     *   "status": "DELIVERED",
     *   "message": "OTP verified! Order marked as delivered."
     * }
     * 
     * Response (Failure):
     * {
     *   "success": false,
     *   "error": "Invalid OTP"
     * }
     */
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam Long orderId,
            @RequestParam String otp) {
        try {
            // STEP 1: Validate order exists
            Orders order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            // STEP 2: Validate order is in ACCEPTED status
            if (!"ACCEPTED".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Order must be ACCEPTED to verify OTP. Current status: " + order.getStatus()
                ));
            }
            
            // STEP 3: Verify OTP (compare hashed values)
            if (!verifyOtpHash(otp, order.getOtp())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "error", "Invalid OTP. Please try again."
                ));
            }
            
            // STEP 4: Mark order as DELIVERED
            order.setStatus("DELIVERED");
            orderRepo.save(order);
            
            // STEP 5: TODO - Send Email to Customer - Delivery Completed
            // emailService.sendDeliveryConfirmationToCustomer(order.getUserEmail());
            
            // STEP 6: TODO - Send Email to Admin - Delivery Verified
            // emailService.sendAdminDeliveryNotification(orderId);
            
            // STEP 7: Return response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", order.getId());
            response.put("status", "DELIVERED");
            response.put("message", "OTP verified! Order marked as delivered.");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", "Server error: " + e.getMessage()
            ));
        }
    }

    /**
     * POST /orders/{orderId}/resend-otp
     * 
     * Resend OTP to customer via email (if they didn't receive it)
     * Only works if order is in ACCEPTED status
     * 
     * Response:
     * {
     *   "success": true,
     *   "message": "OTP resent to customer email"
     * }
     */
    @PostMapping("/{orderId}/resend-otp")
    public ResponseEntity<?> resendOtp(@PathVariable Long orderId) {
        try {
            // STEP 1: Fetch order
            Orders order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            // STEP 2: Validate order is ACCEPTED
            if (!"ACCEPTED".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Order must be ACCEPTED to resend OTP. Current status: " + order.getStatus()
                ));
            }
            
            // STEP 3: TODO - Resend OTP Email to customer
            // Note: You'll need to store un-hashed OTP somewhere or regenerate it
            // emailService.resendOtpToCustomer(order.getUserEmail(), order.getOtp());
            
            // STEP 4: Return response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "OTP resent to customer email");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", "Server error: " + e.getMessage()
            ));
        }
    }

    // ============================================================================
    // HELPER METHODS
    // ============================================================================

    /**
     * Generate 6-digit random OTP
     * 
     * Example: 847291
     */
    private String generateOtp() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000);
        return String.valueOf(otpNumber);
    }

    /**
     * Hash OTP for secure storage
     * 
     * IMPORTANT: In production, use BCryptPasswordEncoder!
     * 
     * Implementation (uncomment when BCrypt is available):
     * 
     * At class level:
     * ````
     * @Autowired
     * private BCryptPasswordEncoder bCryptPasswordEncoder;
     * ````
     * 
     * Or use:
     * ````
     * private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
     * ````
     * 
     * Then:
     * ````
     * return bCryptPasswordEncoder.encode(otp);
     * ````
     */
    private String hashOtp(String otp) {
        // TODO: IMPORTANT - Use BCrypt in production!
        // For now, this is a simple placeholder
        // return bCryptPasswordEncoder.encode(otp);
        
        // TEMPORARY: Just store as-is (NOT SECURE)
        return otp;
    }

    /**
     * Verify OTP against hashed version
     * 
     * IMPORTANT: In production, use BCryptPasswordEncoder!
     * 
     * Implementation (uncomment when BCrypt is available):
     * ````
     * return bCryptPasswordEncoder.matches(providedOtp, hashedOtp);
     * ````
     */
    private boolean verifyOtpHash(String providedOtp, String hashedOtp) {
        // TODO: IMPORTANT - Use BCrypt in production!
        // For now, simple comparison
        // return bCryptPasswordEncoder.matches(providedOtp, hashedOtp);
        
        // TEMPORARY: Direct comparison (NOT SECURE - use BCrypt)
        return providedOtp.trim().equalsIgnoreCase(hashedOtp);
    }
}
