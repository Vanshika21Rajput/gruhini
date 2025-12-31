package com.gruhini.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private String orderId;
    private String razorpayOrderId;
    private BigDecimal amount;
    private String currency;
    private String keyId;
    private LocalDateTime expiresAt;
    private Integer fraudScore;
    private String riskLevel;
}
