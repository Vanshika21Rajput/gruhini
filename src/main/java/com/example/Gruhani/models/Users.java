package com.example.Gruhani.models;

import com.example.Gruhani.Enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Table(name = "Users", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Order> orderList=new ArrayList<>();
    @NotEmpty(message = "Enter valid name")
    @Column(nullable = false)
    private String name;
    @Email(message="EMAIL NOT VALID")
    @Column(unique = true,nullable = false)
    private String email;
    @Size(min=10,max=10,message = "Enter valid mobile number")
    private String contact;
    @Size(min=8,message = "Password size should be 8 characters minimum")
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> role=new HashSet<>();
    @OneToOne(mappedBy = "user",fetch=FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Cart cart;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @OneToOne(mappedBy = "user",fetch=FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Seller seller;
    @OneToOne(mappedBy = "user",fetch=FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    Admin admin;
    @Column(name = "fcm_token")
    private String fcmToken;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,orphanRemoval = true)
    List<PasswordResetOtp>resetOtps;
    @OneToMany(mappedBy = "user",cascade = CascadeType.DETACH,orphanRemoval = true)
    List<Feedback> feedback;


}
