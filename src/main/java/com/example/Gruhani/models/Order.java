package com.example.Gruhani.models;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.dtos.OrderItem;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="Orders")

public class Order {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private  Long id;
    @OneToMany(mappedBy = "order",fetch=FetchType.LAZY)
    List<OrderItem> orderItemList;
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    String deliveryTime;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    Users user;
    @Column(nullable = false)
    LocalDateTime placedAt;
    @Column(nullable = false)
    String deliveryAddress;
    @Column(nullable = false)
    BigInteger orderValue;

    String message;
    @ManyToOne
    @JoinColumn(name="sellerOfOrder")
    Seller seller;
    int otp;

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }




    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(LocalDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public BigInteger getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(BigInteger orderValue) {
        this.orderValue = orderValue;
    }
}
