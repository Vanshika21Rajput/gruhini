package com.gruhini.payment.service;

import com.gruhini.payment.model.Order;
import com.gruhini.payment.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendSecurityAlert(String message, String orderId) {
        log.warn("🚨 SECURITY ALERT SENT: {} | Order: {}", message, orderId);
        // Integrate with Slack/Email/SMS
    }

    public void sendPaymentConfirmation(Order order, Payment payment) {
        log.info("📧 Sending payment confirmation for Order {}", order.getId());
        // Integrate with Email service
    }
}
