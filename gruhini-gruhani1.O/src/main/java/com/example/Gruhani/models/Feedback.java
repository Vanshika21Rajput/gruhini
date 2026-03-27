package com.example.Gruhani.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    Order order;
    @ManyToOne
    @JoinColumn(name = "UserId")
    Users user;
    @ManyToOne
    @JoinColumn(name = "sellerId")
    Seller seller;
    int rating;
    String comment;
    LocalDateTime createdAt;
}
