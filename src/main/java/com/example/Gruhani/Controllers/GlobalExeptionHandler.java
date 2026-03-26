package com.example.Gruhani.Controllers;

import com.example.Gruhani.Exceptions.*;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExeptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity< Map<String,String>> methods(MethodArgumentNotValidException e)
    {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing    // keep first message
                ));
        return ResponseEntity.badRequest().body(errors);
    }
 @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> insufficientstock(InsufficientStockException ex) {
     return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
             "success", false,
             "errorCode", "INSUFFICIENT_STOCK",
             "message", ex.getMessage()
     ));
 }
   @ExceptionHandler(ProductNotFoundException.class)
     public ResponseEntity<?> productNotFound(ProductNotFoundException ex)
     {
         return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                 "success", false,
                 "errorCode", "Product Not Found",
                 "message", ex.getMessage()
         ));


     }
    @ExceptionHandler(InvalidCart.class)
    public ResponseEntity<?> productNotFound(InvalidCart ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "success", false,
                "errorCode", "Cart Not Found",
                "message", ex.getMessage()
        ));
    }
        @ExceptionHandler(StockNotAvailable.class)
        public ResponseEntity<?> productNotFound(StockNotAvailable ex)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "errorCode", "No stock available",
                    "message", ex.getMessage()
            ));


        }
    @ExceptionHandler(InvalidOrder.class)
    public ResponseEntity<?> Invalidorder(InvalidOrder ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "success", false,
                "errorCode", "Order Not Valid",
                "message", ex.getMessage()
        ));
    }
        @ExceptionHandler(  OptimisticLockException.class)
        public ResponseEntity<?> Lock( OptimisticLockException ex)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "errorCode", "RETRY ODERING",
                    "message", ex.getMessage()
            ));
    }
    @ExceptionHandler(  UserNotFoundException.class)
    public ResponseEntity<?> userNotFound( UserNotFoundException ex)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "success", false,
                "errorCode", "RETRY ODERING",
                "message", ex.getMessage()
        ));
    }


    }





