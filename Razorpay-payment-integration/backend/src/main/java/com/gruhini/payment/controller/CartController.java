package com.gruhini.payment.controller;

import com.gruhini.payment.model.Cart;
import com.gruhini.payment.model.CartItem;
import com.gruhini.payment.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;

    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(HttpServletRequest req) {
        // For this demo/integration, we ensure a specific cart (ID 101) exists in the DB
        // so that the PaymentService can fetch it by ID later.
        
        return ResponseEntity.ok(cartRepository.findById(101L).orElseGet(() -> {
            CartItem item1 = CartItem.builder()
                    .productName("Dal Baati Churma")
                    .price(new BigDecimal("350.00"))
                    .quantity(1)
                    .imageUrl("🍛")
                    .build();

            CartItem item2 = CartItem.builder()
                    .productName("Besan Ladoo (500g)")
                    .price(new BigDecimal("450.00"))
                    .quantity(1)
                    .imageUrl("🍬")
                    .build();

            Cart cart = Cart.builder()
                    .id(101L) // Force ID 101 for demo consistency
                    .username("guest_user")
                    .items(List.of(item1, item2))
                    .build();
            
            return cartRepository.save(cart);
        }));
    }
}
