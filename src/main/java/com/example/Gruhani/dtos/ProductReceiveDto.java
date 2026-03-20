package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


    @Getter
    @Setter
    @NoArgsConstructor
    public class ProductReceiveDto {
        String name;
        BigDecimal price;
        Category category;
        String subcategory;
        String description;
        int stock;
        Double discount;
        String deliveryTime;

    }

