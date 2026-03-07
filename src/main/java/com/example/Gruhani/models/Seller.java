package com.example.Gruhani.models;
import com.example.Gruhani.Enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Seller")
@Getter
@Setter
@NoArgsConstructor
public class Seller {
    @GeneratedValue(
            strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String address;
    @Column(nullable = false)
    private String contactNo;
    Boolean isApproved=false;
    @Column(nullable = false)
    private String businessName;
    @JoinColumn(name="user_id")
    @OneToOne(fetch = FetchType.LAZY)
   private Users user;


    @OneToMany(mappedBy = "seller",fetch=FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Product> products=new ArrayList<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
     private List<Category> categories=new ArrayList<>();

    int totalOrderCount=0;


}
