package com.example.Gruhani.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Address {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String addressLine;
    String pincode;
    String state;
    String city;
    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;


}
