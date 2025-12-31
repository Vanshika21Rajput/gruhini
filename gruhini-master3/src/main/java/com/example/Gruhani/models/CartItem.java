package com.example.Gruhani.models;

import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
public class CartItem {
    @ManyToOne( fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    product p;
    @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
    Long  id;
    @ManyToOne
    @JoinColumn(name="cart_id") //by default it references primary key column but you can use "referencedColumn=column_name"
    Cart c;
    int quantity;
    BigInteger priceAtAddTime;

    public product getP() {
        return p;
    }

    public void setP(product p) {
        this.p = p;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getC() {
        return c;
    }

    public void setC(Cart c) {
        this.c = c;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigInteger getPriceAtAddTime() {
        return priceAtAddTime;
    }

    public void setPriceAtAddTime(BigInteger priceAtAddTime) {
        this.priceAtAddTime = priceAtAddTime;
    }
}
