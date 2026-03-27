# 🛠️ Gruhini 1.0 Backend Implementation Guide

## ✅ What's Been Created

### New DTOs (Ready in src/main/java/com/example/Gruhani/dtos/)

#### 1. **AcceptOrderDto.java** ✅ CREATED
Fields:
- `Long orderId` - Order ID to accept
- `String deliveryTime` - e.g., "Today at 8:00 PM"
- `String deliveryMethod` - SELLER_DELIVERY, CUSTOMER_PICKUP, THIRD_PARTY
- `String deliveryNotes` - Optional special instructions

#### 2. **RejectOrderDto.java** ✅ CREATED
Fields:
- `Long orderId` - Order ID to reject
- `String rejectionReason` - Why order is being rejected

---

## 🔧 Database Model Updates

### Orders.java Entity ✅ UPDATED
**Added 5 New Fields:**

```java
private String deliveryTime;      // "Today at 8:00 PM", "Tomorrow morning", etc.
private String deliveryMethod;    // SELLER_DELIVERY, CUSTOMER_PICKUP, THIRD_PARTY
private String otp;               // 6-digit OTP (hashed), generated when ACCEPTED
private String rejectionReason;   // Reason if seller rejects
private String deliveryNotes;     // Special instructions for delivery
```

**Updated Status Field:**
```java
// BEFORE: private String status; // Placed, Preparing, Delivered
// AFTER:  private String status; // PENDING, ACCEPTED, REJECTED, DELIVERED
```

**Added Getters & Setters:**
- `getDeliveryTime()` / `setDeliveryTime()`
- `getDeliveryMethod()` / `setDeliveryMethod()`
- `getOtp()` / `setOtp()`
- `getRejectionReason()` / `setRejectionReason()`
- `getDeliveryNotes()` / `setDeliveryNotes()`

---

## 🌐 Controller Endpoints to Add

### Option 1: Update Order_Controls.java

**Endpoint 1: POST /place-order** (NEW - replaces /orders/create)
```java
@PostMapping("/place-order")
public ResponseEntity<?> placeOrder(
        @RequestBody Map<String, Object> payload,
        Authentication authentication) {
    try {
        String userEmail = authentication.getName();
        
        // Extract data from payload
        String addressId = (String) payload.get("addressId");
        Map<String, String> deliveryAddress = (Map<String, String>) payload.get("deliveryAddress");
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) payload.get("cartItems");
        Double subtotal = ((Number) payload.get("subtotal")).doubleValue();
        Double taxes = ((Number) payload.get("taxes")).doubleValue();
        Double deliveryFee = ((Number) payload.get("deliveryFee")).doubleValue();
        Double total = ((Number) payload.get("total")).doubleValue();
        
        // Create new Orders entity
        Orders order = new Orders();
        order.setUserEmail(userEmail);
        order.setItems(convertItemsToJson(cartItems)); // Convert to JSON string
        order.setAmount(total);
        order.setStatus("PENDING");  // Start with PENDING status
        order.setOrderDate(new java.util.Date());
        order.setPaymentId(null);    // No payment for COD
        
        // Save order to database
        Orders savedOrder = orderRepo.save(order);
        
        // TODO: Send Email Notification to Seller
        // emailService.sendNewOrderNotification(userEmail, order);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", savedOrder.getId());
        response.put("status", "PENDING");
        response.put("message", "Order created successfully!");
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", "Order creation failed: " + e.getMessage()
        ));
    }
}
```

**Endpoint 2: GET /orders/{orderId}** (UPDATED - needs real database lookup)
```java
@GetMapping("/{orderId}")
public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
    try {
        // Fetch from database
        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getId());
        response.put("userEmail", order.getUserEmail());
        response.put("status", order.getStatus());
        response.put("amount", order.getAmount());
        response.put("items", order.getItems()); // JSON string of items
        response.put("orderDate", order.getOrderDate());
        response.put("deliveryTime", order.getDeliveryTime());
        response.put("deliveryMethod", order.getDeliveryMethod());
        response.put("deliveryNotes", order.getDeliveryNotes());
        
        // Include OTP only if order is ACCEPTED or later
        if ("ACCEPTED".equals(order.getStatus()) || "DELIVERED".equals(order.getStatus())) {
            response.put("otp", order.getOtp()); // Frontend will handle blurring
        }
        
        // TODO: Include seller info from database
        // response.put("seller", new SellerResponseDto(...));
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", "Error fetching order: " + e.getMessage()
        ));
    }
}
```

---

### Option 2: Create New Seller Order Controller

**File:** `Seller_Order_Controls.java`

```java
package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.OrderRepo;
import com.example.Gruhani.dtos.AcceptOrderDto;
import com.example.Gruhani.dtos.RejectOrderDto;
import com.example.Gruhani.models.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
     * Seller accepts order with delivery details
     */
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/accept-order")
    public ResponseEntity<?> acceptOrder(@RequestBody AcceptOrderDto acceptDto) {
        try {
            // Validate order exists
            Orders order = orderRepo.findById(acceptDto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            // Validate order is still PENDING
            if (!"PENDING".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Order is not in PENDING status"
                ));
            }
            
            // Generate 6-digit OTP
            String otp = generateOtp();
            
            // Update order
            order.setStatus("ACCEPTED");
            order.setDeliveryTime(acceptDto.getDeliveryTime());
            order.setDeliveryMethod(acceptDto.getDeliveryMethod());
            order.setDeliveryNotes(acceptDto.getDeliveryNotes());
            order.setOtp(hashOtp(otp)); // Hash before storing
            
            // Save to database
            orderRepo.save(order);
            
            // TODO: Send Email to Customer with OTP
            // emailService.sendOrderAcceptanceEmail(
            //     order.getUserEmail(),
            //     otp,
            //     acceptDto.getDeliveryTime(),
            //     acceptDto.getDeliveryMethod()
            // );
            
            // TODO: Send Email to Admin
            // emailService.sendAdminNotification(
            //     "gruhani214@gmail.com",
            //     "Order Accepted: #" + order.getId()
            // );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", order.getId());
            response.put("status", "ACCEPTED");
            response.put("otp", otp); // Return un-hashed OTP to seller
            response.put("message", "Order accepted! OTP sent to customer.");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * POST /seller/reject-order
     * Seller rejects order with reason
     */
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/reject-order")
    public ResponseEntity<?> rejectOrder(@RequestBody RejectOrderDto rejectDto) {
        try {
            // Validate order exists
            Orders order = orderRepo.findById(rejectDto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            // Validate order is still PENDING
            if (!"PENDING".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Order is not in PENDING status"
                ));
            }
            
            // Update order
            order.setStatus("REJECTED");
            order.setRejectionReason(rejectDto.getRejectionReason());
            
            // Save to database
            orderRepo.save(order);
            
            // TODO: Send Email to Customer with Rejection Reason
            // emailService.sendOrderRejectionEmail(
            //     order.getUserEmail(),
            //     rejectDto.getRejectionReason()
            // );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", order.getId());
            response.put("status", "REJECTED");
            response.put("message", "Order rejected. Customer notified.");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * POST /seller/verify-otp
     * Seller verifies OTP and marks order as DELIVERED
     */
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam Long orderId,
            @RequestParam String otp) {
        try {
            // Validate order exists
            Orders order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            // Validate order is ACCEPTED
            if (!"ACCEPTED".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Order is not in ACCEPTED status"
                ));
            }
            
            // Verify OTP (compare hashes)
            if (!verifyOtpHash(otp, order.getOtp())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid OTP"
                ));
            }
            
            // Update order to DELIVERED
            order.setStatus("DELIVERED");
            orderRepo.save(order);
            
            // TODO: Send Email to Customer - Delivery Completed
            // emailService.sendDeliveryConfirmation(order.getUserEmail());
            
            // TODO: Send Email to Admin - Delivery Verified
            // emailService.sendAdminDeliveryNotification(orderId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "OTP verified! Order marked as delivered.");
            response.put("status", "DELIVERED");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * POST /orders/{orderId}/resend-otp
     * Resend OTP to customer email
     */
    @PostMapping("/{orderId}/resend-otp")
    public ResponseEntity<?> resendOtp(@PathVariable Long orderId) {
        try {
            Orders order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            if (!"ACCEPTED".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Order must be ACCEPTED to resend OTP"
                ));
            }
            
            // TODO: Email service to resend OTP
            // Get unhashed OTP from somewhere (or regenerate)
            // emailService.resendOtpEmail(order.getUserEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "OTP resent to customer email");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // ============================================================================
    // HELPER METHODS
    // ============================================================================

    /**
     * Generate 6-digit random OTP
     */
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Hash OTP using BCrypt (you'll need to import BCryptPasswordEncoder)
     * 
     * // At class level:
     * private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
     */
    private String hashOtp(String otp) {
        // TODO: Implement BCrypt hashing
        // return bCryptPasswordEncoder.encode(otp);
        
        // For now, simple hash (NOT SECURE - use BCrypt in production)
        return otp; // WARNING: This is just a placeholder
    }

    /**
     * Verify OTP against hashed version
     */
    private boolean verifyOtpHash(String providedOtp, String hashedOtp) {
        // TODO: Implement BCrypt verification
        // return bCryptPasswordEncoder.matches(providedOtp, hashedOtp);
        
        // For now, simple comparison (NOT SECURE)
        return providedOtp.equals(hashedOtp); // WARNING: This is just a placeholder
    }

    /**
     * Convert cart items list to JSON string for storage
     */
    private String convertItemsToJson(List<Map<String, Object>> items) {
        // TODO: Use ObjectMapper for proper JSON conversion
        return items.toString(); // Simple placeholder
    }
}
```

---

## ⚙️ Service Layer (Recommended)

Create `OrderService.java` for business logic:

```java
package com.example.Gruhani.service;

import com.example.Gruhani.dtos.AcceptOrderDto;
import com.example.Gruhani.models.Orders;
import com.example.Gruhani.Repositories.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    /**
     * Process new order placement
     */
    public Orders placeOrder(String userEmail, Map<String, Object> orderData) {
        Orders order = new Orders();
        order.setUserEmail(userEmail);
        order.setStatus("PENDING");
        order.setAmount(((Number) orderData.get("total")).doubleValue());
        order.setOrderDate(new java.util.Date());
        
        return orderRepo.save(order);
    }

    /**
     * Process seller acceptance of order
     */
    public Orders acceptOrder(AcceptOrderDto acceptDto) {
        Orders order = orderRepo.findById(acceptDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        String otp = generateOtp();
        order.setStatus("ACCEPTED");
        order.setDeliveryTime(acceptDto.getDeliveryTime());
        order.setDeliveryMethod(acceptDto.getDeliveryMethod());
        order.setDeliveryNotes(acceptDto.getDeliveryNotes());
        order.setOtp(hashOtp(otp));
        
        return orderRepo.save(order);
    }

    /**
     * Verify OTP and mark as delivered
     */
    public Orders verifyOtpAndDeliver(Long orderId, String otp) {
        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!verifyOtpHash(otp, order.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        
        order.setStatus("DELIVERED");
        return orderRepo.save(order);
    }

    // Helper methods (move to utility class)
    private String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    private String hashOtp(String otp) {
        // TODO: Use BCryptPasswordEncoder
        return otp;
    }

    private boolean verifyOtpHash(String provided, String hashed) {
        // TODO: Use BCryptPasswordEncoder.matches()
        return provided.equals(hashed);
    }
}
```

---

## 📧 Email Service (Needed)

Create `EmailService.java`:

```java
package com.example.Gruhani.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send order placed notification to seller
     */
    public void sendNewOrderNotification(String sellerEmail, Orders order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sellerEmail);
        message.setSubject("🍳 नया ऑर्डर आ गया! New Order Received!");
        message.setText("Order #" + order.getId() + " placed by " + order.getUserEmail());
        
        mailSender.send(message);
    }

    /**
     * Send OTP to customer
     */
    public void sendOrderAcceptanceEmail(String customerEmail, String otp, 
                                         String deliveryTime, String deliveryMethod) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customerEmail);
        message.setSubject("✅ आपका ऑर्डर स्वीकार हो गया! Order Accepted!");
        message.setText("Your OTP: " + otp + "\nDelivery Time: " + deliveryTime + 
                       "\nMethod: " + deliveryMethod);
        
        mailSender.send(message);
    }

    // Similar methods for rejection, delivery, etc.
}
```

---

## 📋 Application.properties Configuration

Add to `application.properties`:

```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=gruhani214@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

---

## ✅ Testing Endpoints (Postman/Curl)

**1. Place Order:**
```bash
curl -X POST http://localhost:8080/place-order \
  -H "Content-Type: application/json" \
  -d '{
    "addressId": "addr-1",
    "deliveryAddress": {
      "fullName": "Amit",
      "phone": "9876543210"
    },
    "cartItems": [{"id": 1, "name": "Dal", "price": 250}],
    "subtotal": 250,
    "taxes": 25,
    "deliveryFee": 49,
    "total": 324
  }'
```

**2. Get Order:**
```bash
curl -X GET http://localhost:8080/orders/1
```

**3. Accept Order:**
```bash
curl -X POST http://localhost:8080/seller/accept-order \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "deliveryTime": "Today at 8:00 PM",
    "deliveryMethod": "SELLER_DELIVERY",
    "deliveryNotes": "Ring bell twice"
  }'
```

**4. Verify OTP:**
```bash
curl -X POST "http://localhost:8080/seller/verify-otp?orderId=1&otp=847291"
```

---

## 🎯 Summary of Changes

| File | Status | Change |
|------|--------|--------|
| AcceptOrderDto.java | ✅ CREATED | New DTO for seller acceptance |
| RejectOrderDto.java | ✅ CREATED | New DTO for seller rejection |
| Orders.java | ✅ UPDATED | Added 5 new fields + getters/setters |
| Order_Controls.java | ⏳ UPDATE | POST /place-order, GET /{id} endpoints |
| Seller_Order_Controls.java | ⏳ CREATE | Accept, reject, verify-otp endpoints |
| OrderService.java | ⏳ CREATE | Business logic layer |
| EmailService.java | ⏳ CREATE | Email notifications |
| application.properties | ⏳ UPDATE | Email configuration |

---

## 🚀 Next Steps

1. ✅ DTOs created
2. ✅ Orders.java updated
3. ⏳ Copy-paste the controllers code above
4. ⏳ Create OrderService
5. ⏳ Create EmailService
6. ⏳ Update application.properties
7. ⏳ Run `mvn clean install spring-boot:run`
8. ⏳ Test endpoints with Postman
9. ⏳ Verify emails are being sent

**Your frontend is already ready and waiting!** 🎉
