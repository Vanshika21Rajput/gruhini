package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SellerSummaryDto {
        private Long id;
        private String businessName;
        private String profileImageUrl;
        private List<Category> category;
        private Float rating;
}
