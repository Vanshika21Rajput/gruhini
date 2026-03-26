package com.example.Gruhani.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name ="userid")
    Users user;
    @OneToMany(
            mappedBy = "cart",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private List<CartItem> cartItems=new ArrayList<>();
    LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate //Each time before entity is updated into DB this field is set to the new value
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();

    }
   


}
