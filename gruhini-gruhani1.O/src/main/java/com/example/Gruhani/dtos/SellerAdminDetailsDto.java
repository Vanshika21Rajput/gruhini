package com.example.Gruhani.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SellerAdminDetailsDto {

        private Long sellerId;
        private String sellerName;
        private String businessName;
        private String contact;
        private String image;


        private Long totalOrders;
        private Long deliveredOrders;
        private Long pendingOrders;
        private BigDecimal totalRevenue;
        private Boolean isApproved;
    }
