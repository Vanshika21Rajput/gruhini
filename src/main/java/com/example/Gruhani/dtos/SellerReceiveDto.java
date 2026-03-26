package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SellerReceiveDto {
@NotBlank
    String name;
@Email(message="Email must be in valid format  ")
     String email;
@Size(min=10)
      String phone;
String businessName;
    List<Category> categories;
String description;
@NotNull
@Valid
    AddressDto addressDto;

}
//receiving from seller and viewng full seller details
