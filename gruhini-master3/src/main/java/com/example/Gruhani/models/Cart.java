package com.example.Gruhani.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
public class Cart {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;

    @OneToOne
    @JoinColumn(name ="userid")
    Users u;
    @OneToMany(
            mappedBy = "c",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<CartItem> l;
    LocalDateTime updadtedAt;

    public Long getId() {
        return id;
    }



    public Users getU() {
        return u;
    }

    public void setU(Users u) {
        this.u = u;
    }

    public List<CartItem> getL() {
        return l;
    }

    public void setL(List<CartItem> l) {
        this.l = l;
    }

    public LocalDateTime getUpdadtedAt() {
        return updadtedAt;
    }

    public void setUpdadtedAt(LocalDateTime updadtedAt) {
        this.updadtedAt = updadtedAt;
    }
}
