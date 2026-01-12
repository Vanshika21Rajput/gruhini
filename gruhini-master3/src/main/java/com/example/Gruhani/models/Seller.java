package com.example.Gruhani.models;
import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "seller", schema = "public")
public class Seller {
    @GeneratedValue(
            strategy = GenerationType.AUTO)
    @Id
    Long id;
    String address;
    String contactNo;
    Boolean isApproved;
    String BusinessName;
    @JoinColumn(name="user_id")
    @OneToOne
    Users user;

    @OneToMany(mappedBy = "seller",fetch=FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    List<product> pr;
     List<String> categories;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    // NEW FIELDS for dynamic seller profiles
    String bio;
    Integer yearsExperience;
    String avatar;
    String location;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
