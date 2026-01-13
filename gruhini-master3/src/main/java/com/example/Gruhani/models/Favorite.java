package com.example.Gruhani.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userEmail;
    private String dishId;
    private String dishName;
    private String dishImage;
    private Double dishPrice;
    private String chefName;

    public Favorite() {}

    public Favorite(String userEmail, String dishId, String dishName, String dishImage, Double dishPrice, String chefName) {
        this.userEmail = userEmail;
        this.dishId = dishId;
        this.dishName = dishName;
        this.dishImage = dishImage;
        this.dishPrice = dishPrice;
        this.chefName = chefName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getDishId() { return dishId; }
    public void setDishId(String dishId) { this.dishId = dishId; }
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public String getDishImage() { return dishImage; }
    public void setDishImage(String dishImage) { this.dishImage = dishImage; }
    public Double getDishPrice() { return dishPrice; }
    public void setDishPrice(Double dishPrice) { this.dishPrice = dishPrice; }
    public String getChefName() { return chefName; }
    public void setChefName(String chefName) { this.chefName = chefName; }
}
