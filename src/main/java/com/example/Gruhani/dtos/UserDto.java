package com.example.Gruhani.dtos;

import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    public String email;
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact must be 10 digits")
    String contact;
    @Size(min=6,message = "Enter password of atleast 6 characters")
    String password;
    @Valid
    AddressDto addressDto;
}
