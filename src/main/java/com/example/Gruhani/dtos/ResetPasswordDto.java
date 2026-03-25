package com.example.Gruhani.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordDto {
    private String email;
    private String otp;
    private String newPassword;
}
