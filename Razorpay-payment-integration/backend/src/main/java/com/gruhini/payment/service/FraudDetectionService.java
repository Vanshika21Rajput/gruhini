package com.gruhini.payment.service;

import com.gruhini.payment.dto.OrderRequest;
import com.gruhini.payment.model.Order;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@Slf4j
public class FraudDetectionService {

    public FraudScore analyze(OrderRequest request, String ipAddress) {
        log.info("Analyzing fraud risk for IP: {}", ipAddress);
        
        // Mock Logic for Demo
        // In production, this would call external risk APIs
        
        int score = 0;
        RiskLevel riskLevel = RiskLevel.LOW;
        
        // Rule 1: High amount check
        if (request.getAmount().compareTo(BigDecimal.valueOf(50000)) > 0) {
            score += 40;
        }
        
        // Rule 2: Random score for demo variability
        score += new Random().nextInt(20);
        
        if (score > 80) riskLevel = RiskLevel.HIGH;
        else if (score > 50) riskLevel = RiskLevel.MEDIUM;
        
        return FraudScore.builder()
                .score(score)
                .riskLevel(riskLevel)
                .build();
    }
    
    @Data
    @Builder
    public static class FraudScore {
        private int score;
        private RiskLevel riskLevel;
    }
    
    public enum RiskLevel {
        LOW, MEDIUM, HIGH
    }
}
