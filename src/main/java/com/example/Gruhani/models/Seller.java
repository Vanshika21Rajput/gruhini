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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;
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
    @CollectionTable(name = "seller_categories", joinColumns = @JoinColumn(name = "seller_id"))
     private List<Category> categories=new ArrayList<>();
    @OneToMany(mappedBy = "seller",fetch=FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Order> orders=new ArrayList<>();
    private int totalOrderCount=0;
    private Float rating;
    private String Description;
    @OneToMany(mappedBy = "seller",cascade = CascadeType.DETACH,fetch = FetchType.LAZY,orphanRemoval = true)
    List<Feedback> feedback;



}
