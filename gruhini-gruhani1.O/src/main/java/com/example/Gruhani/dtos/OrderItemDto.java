package com.example.Gruhani.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {
    String productName;
    String productImage;
    int quantity;
    BigDecimal priceAtOrderTime;
}
