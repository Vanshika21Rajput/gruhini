package com.example.Gruhani.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class sellerLogindto {
    @Email(message = "email is not valid")
    String username;
    @Size(min=10)
    String password;
    String userType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
