package com.example.Gruhani.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto {
    Long id=0l;
    String addressLine;
    String pincode;
    String state;
    String city;
}
