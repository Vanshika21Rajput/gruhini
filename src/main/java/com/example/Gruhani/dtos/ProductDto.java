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
    Category category;
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
}
