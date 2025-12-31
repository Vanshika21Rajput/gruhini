/*package com.example.Gruhani.models;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.dtos.OrderItem;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Component
public class Order {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private  Long id;
    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItemList;
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    String DeliveryTime;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    Users user;
    LocalDateTime placedAt;
    String deliveryAddress;
    int OrderValue;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="seller_id")
    Seller seller;
    String message;

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

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public Users getU() {
        return user;
    }

    public void setU(Users u) {
        this.user = u;
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

    public int getOrderValue() {
        return OrderValue;
    }

    public void setOrderValue(int orderValue) {
        OrderValue = orderValue;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}*/
