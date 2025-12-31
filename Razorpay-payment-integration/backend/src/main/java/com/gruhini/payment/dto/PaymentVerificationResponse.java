package com.gruhini.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentVerificationResponse {
    private boolean success;
    private String message;
    private String orderId;
    private String paymentId;
    private BigDecimal amount;
    private String errorCode;
}
