package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.Category;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SellerDto {
@NotBlank
    String name;
@Email(message="Email must be in valid format  ")
     String email;
@Size(min=10)
      String phone;
String businessName;
    List<Category> categories;

}
