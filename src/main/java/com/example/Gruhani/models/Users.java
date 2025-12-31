package com.example.Gruhani.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users", schema = "public")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @OneToMany(mappedBy = "user")
    List<Order> orderList;
    String name;
    @Column(unique = true)
    String email;
    String contact;
    String password;
    @ElementCollection(fetch = FetchType.EAGER)

    Set<String> role;
    String address;
    @OneToOne(mappedBy = "u",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    Cart cart;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public void setid(Long string) {
        this.id=string;
    }

    public Long getId() {
        return id;
    }
}
