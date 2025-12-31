package com.gruhini.payment.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(nullable = false)
    private String razorpayOrderId;
    
    @Column
    private String razorpayPaymentId;
    
    @Column
    private String razorpaySignature;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private PaymentMethod method; // Nullable until payment is completed
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column
    private String upiId;  // For UPI payments
    
    @Column
    private String cardLast4;  // Last 4 digits of card
    
    @Column(name = "metadata", columnDefinition = "text")
    private String metadata;  // Additional payment info
    
    @Column
    private LocalDateTime paidAt;
    
    @Column
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = PaymentStatus.CREATED;
    }
}
