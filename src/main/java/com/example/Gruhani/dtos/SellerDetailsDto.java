package com.example.Gruhani.dtos;

import com.example.Gruhani.models.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class SellerDetailsDto {
    String name;
    String contact;
    Address address;
    String businessName;
}
