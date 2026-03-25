package com.example.Gruhani.dtos;

import com.example.Gruhani.models.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
public class SellerDetailsDto {
    Long id;
    String name;
    String contact;
    AddressDto address;
    String businessName;
    String image;
    String Description;
}
