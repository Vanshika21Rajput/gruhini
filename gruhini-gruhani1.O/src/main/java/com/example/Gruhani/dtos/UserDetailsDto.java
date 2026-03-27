package com.example.Gruhani.dtos;


import com.example.Gruhani.Enums.Role;
import com.example.Gruhani.models.Address;
import com.example.Gruhani.models.Cart;
import com.example.Gruhani.models.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class UserDetailsDto {
    String contact;
    String email;
    String name;

}
