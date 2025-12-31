package com.gruhini.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentStatusResponse {
    private String orderId;
    private String paymentId;
    private String status;
    private String message;
    private BigDecimal amount;
    private Long timeRemaining;
    private Integer fraudScore;
}
