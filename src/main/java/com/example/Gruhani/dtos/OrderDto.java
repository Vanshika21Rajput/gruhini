package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    // Order info
    Long orderId;
    LocalDateTime placedAt;
    BigDecimal orderValue;
    OrderStatus status;
    String deliveryTime;
    String message;

    // Buyer info
    String buyerName;
    String buyerEmail;

    // Seller info
    String sellerName;
    String sellerBusinessName;
    String sellerPhone;

    // Items
    List<OrderItemDto> items;

    // Delivery
    String addressLine;
    String city;
    String state;
    String pincode;
}