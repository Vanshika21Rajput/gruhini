package com.example.Gruhani.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userEmail;
    private String chefName;
    
    @Column(length = 1000)
    private String items; // JSON string or comma separated
    
    private Double amount;
    private String paymentId; // Razorpay payment ID
    private String status; // Placed, Preparing, Delivered
    private Date orderDate;

    public Orders() {}

    public Orders(String userEmail, String chefName, String items, Double amount, String paymentId, String status, Date orderDate) {
        this.userEmail = userEmail;
        this.chefName = chefName;
        this.items = items;
        this.amount = amount;
        this.paymentId = paymentId;
        this.status = status;
        this.orderDate = orderDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getChefName() { return chefName; }
    public void setChefName(String chefName) { this.chefName = chefName; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}
