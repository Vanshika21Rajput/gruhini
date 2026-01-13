package com.example.Gruhani.Package;

public class InsufficientStockException extends  RuntimeException{
    public InsufficientStockException(String message)
    {
        super(message);
    }
}
