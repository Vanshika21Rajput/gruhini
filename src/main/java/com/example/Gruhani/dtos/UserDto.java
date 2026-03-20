package com.example.Gruhani.dtos;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    @NotBlank(message="Name cannot be Empty")
    String name;
    @Email(message="Enter Valid Email")
    public String email;
    String contact;
    String password;
    AddressDto addressDto;
}
