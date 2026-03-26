package com.example.Gruhani.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;


}
