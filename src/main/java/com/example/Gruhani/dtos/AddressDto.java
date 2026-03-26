package com.example.Gruhani.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto {
    Long id=0l;
    @NotBlank
    String addressLine;
    @NotBlank
    String pincode;
    @NotBlank
    String state;
    @NotBlank
    String city;
}
