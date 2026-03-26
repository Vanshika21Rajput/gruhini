package com.example.Gruhani.Exceptions;

public class InsufficientStockException extends  RuntimeException{
    public InsufficientStockException(String message)
    {
        super(message);
    }
}
