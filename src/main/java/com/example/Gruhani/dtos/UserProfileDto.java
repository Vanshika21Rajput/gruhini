package com.example.Gruhani.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileDto {
    private Long id;
    private String name;
    private String email;
    private String contact;
    private String profileImageUrl;
    private List<AddressDto> addresses;
}
