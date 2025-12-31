package com.gruhini.payment.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100)
    private String customerName;
    
    @NotBlank @Email
    private String customerEmail;
    
    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    private String customerPhone;
    
    @NotEmpty
    private List<CartItem> items;
    
    @NotNull
    @DecimalMin(value = "1.00")
    private BigDecimal amount;

    // Optional: For server-side recalculation of amount
    private Long cartId;
    
    @Data
    public static class CartItem {
        private String productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
    }
}
