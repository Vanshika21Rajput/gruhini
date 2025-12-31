package com.gruhini.payment.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String event;
    
    @Column(nullable = false)
    private String ipAddress;
    
    @Column(columnDefinition = "text")
    private String data; // JSON string of validation data/errors
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
