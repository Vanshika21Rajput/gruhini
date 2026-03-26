package com.example.Gruhani.Exceptions;

public class StockNotAvailable extends RuntimeException {
    public StockNotAvailable(String message) {
        super(message);
    }
}
