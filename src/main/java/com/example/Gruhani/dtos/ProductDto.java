package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.Category;
import com.example.Gruhani.Enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    Long id;
   String  name;
   BigDecimal price;
    Category categories;
    String subcategory;
    String  description;
     int stock;
   ProductStatus status=ProductStatus.PENDING;
  Double rating;
   Double discount;
    Boolean verified=false;
     String message;
     String deliveryTime;
     String badge;
     Long sellerId;
     String image;
    public ProductDto(
            Long id,
            String name,
            BigDecimal price,
            Category categories,
            String subcategory,
            String description,
            int stock,
            ProductStatus status,
            Double rating,
            Double discount,
            Boolean verified,
            String message,
            String deliveryTime,
            String badge,
            Long sellerId,
            String image
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categories = categories;
        this.subcategory = subcategory;
        this.description = description;
        this.stock = stock;
        this.status = status;
        this.rating = rating;
        this.discount = discount;
        this.verified = verified;
        this.message = message;
        this.deliveryTime = deliveryTime;
        this.badge = badge;
        this.sellerId = sellerId;
        this.image = image;
    }
}
