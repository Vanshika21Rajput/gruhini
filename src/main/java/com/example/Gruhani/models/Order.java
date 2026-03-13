package com.example.Gruhani.models;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.models.OrderItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_seller_id", columnList = "seller_id"),
        @Index(name = "idx_order_user_id", columnList = "user_id"),
        @Index(name = "idx_order_status", columnList = "orderStatus")
})
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private  Long id;
    @OneToMany(mappedBy = "order",fetch=FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItemList;
    @Enumerated(EnumType.STRING)
   private OrderStatus orderStatus;
    private String deliveryTime;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    Users user;
    @Column(nullable = false)
    LocalDateTime placedAt;
    @Column(nullable = false)
    BigDecimal orderValue;

    private String message;
    @ManyToOne
    @JoinColumn(name="sellerOfOrder")
    Seller seller;
    @Size(min=6,max=6)
    String hashedOtp;
    @NotNull
    LocalDateTime expiration;
    Boolean OtpVerified=false;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;



}
