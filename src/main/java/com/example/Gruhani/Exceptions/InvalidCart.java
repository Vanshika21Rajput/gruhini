package com.example.Gruhani.Exceptions;

public class InvalidCart extends RuntimeException {
    public InvalidCart(String message) {
        super(message);
    }
}
