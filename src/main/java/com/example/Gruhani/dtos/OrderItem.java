/*package com.example.Gruhani.dtos;

import com.example.Gruhani.models.Order;
import com.example.Gruhani.models.product;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
@Entity
@Component
public class OrderItem {
    @Id
     @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;
    int quantity;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="orderReference")
    Order order;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="productReference")
    product p;
    BigInteger priceAtOrderTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order= order;
    }

    public product getP() {
        return p;
    }

    public void setP(product p) {
        this.p = p;
    }

    public BigInteger getPriceAtOrderTime() {
        return priceAtOrderTime;
    }

    public void setPriceAtOrderTime(BigInteger priceAtOrderTime) {
        this.priceAtOrderTime = priceAtOrderTime;
    }
}*/
