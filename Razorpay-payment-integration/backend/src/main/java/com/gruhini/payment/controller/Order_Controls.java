package com.gruhini.payment.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin // Allow frontend access
public class Order_Controls {

    // 🔴 TODO: Move these to application.properties for security
    private final String KEY_ID = "rzp_test_YOUR_KEY_HERE";
    private final String KEY_SECRET = "YOUR_SECRET_HERE";

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data) {
        try {
            // Amount comes in INR, Razorpay needs Paise (multiply by 100)
            int amount = Integer.parseInt(data.get("amount").toString());

            RazorpayClient razorpay = new RazorpayClient(KEY_ID, KEY_SECRET);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = razorpay.orders.create(orderRequest);

            return ResponseEntity.ok(Map.of(
                "id", order.get("id").toString(),
                "amount", order.get("amount"),
                "status", "created"
            ));

        } catch (RazorpayException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Server Error"));
        }
    }
}
