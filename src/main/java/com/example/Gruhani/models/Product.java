package com.example.Gruhani.models;

import com.example.Gruhani.Enums.Category;
import com.example.Gruhani.Enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String name;
    private String description;
   private Boolean verified=false;
   @Min(value=0,message = "Price can't be negative")
  private BigDecimal price;
   private Double rating;
    private String badge;
    @Column(nullable = false)
    @NotBlank
    private String deliveryTime;
    @NotBlank
    private String image;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String subcategory;
    @Min(value = 0,message = "Stock cannot be negative")
    private int stock;
    private Double discount;
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CartItem> cartItems=new ArrayList<>();
    @OneToMany(mappedBy ="product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="seller_id",nullable =false)
    private Seller seller;

    String message;
    @Version
    private Long version;




}
