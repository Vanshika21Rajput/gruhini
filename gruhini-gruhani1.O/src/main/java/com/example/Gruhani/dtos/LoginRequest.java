package com.example.Gruhani.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

@Component
public class LoginRequest {
    @Email(message = "must be valid mail")
     String email;
    @Size(min=5)
     String password;
    String userType;

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return email;
    }

    public void setEmail(String username) {
      email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
