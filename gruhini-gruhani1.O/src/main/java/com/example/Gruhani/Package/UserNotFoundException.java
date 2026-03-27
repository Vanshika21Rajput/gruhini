package com.example.Gruhani.Package;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException (String message)
    {
        super(message);
    }
}
