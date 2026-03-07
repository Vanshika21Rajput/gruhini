package com.example.Gruhani.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="cartItems")
public class CartItem {

    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cart_id") //by default it references primary key column but you can use "referencedColumn=column_name"
    private Cart cart;
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Min(value = 1,message ="Atleast one unit must be selected")
    @Column(nullable = false)
    private int quantity;
    @Min(value = 0,message ="No negative price allowed")
    @Column(nullable = false)
   private BigDecimal priceAtAddTime;


}
