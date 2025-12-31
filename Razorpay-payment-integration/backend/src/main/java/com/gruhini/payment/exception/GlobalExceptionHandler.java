package com.gruhini.payment.exception;

import com.gruhini.payment.dto.PaymentVerificationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(PaymentException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "PAYMENT_ERROR"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FraudException.class)
    public ResponseEntity<ErrorResponse> handleFraudException(FraudException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "FRAUD_DETECTED"), HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitException(RateLimitException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "RATE_LIMIT_EXCEEDED"), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred", "INTERNAL_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
        private String errorCode;
    }
}
