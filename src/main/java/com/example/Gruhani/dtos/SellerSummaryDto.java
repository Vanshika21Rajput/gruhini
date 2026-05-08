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
        private Float rating;
    public SellerSummaryDto(Long id,
                            String businessName,
                            String profileImageUrl,
                            Float rating) {
        this.id = id;
        this.businessName = businessName;
        this.profileImageUrl = profileImageUrl;
        this.rating = rating;
    }
}
