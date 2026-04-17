package com.example.Gruhani.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendOtpEmailHtml(String toEmail, String otp, String orderId)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("gruhani214@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Gruhani Order OTP - #" + orderId);

        String htmlContent =
                "<div style='font-family:Arial; max-width:500px; margin:auto;'>" +
                        "  <h2 style='color:#e91e63;'>Gruhani 🛒</h2>" +
                        "  <p>Your OTP for <b>Order #" + orderId + "</b> is:</p>" +
                        "  <div style='font-size:32px; font-weight:bold; color:#333;" +
                        "       letter-spacing:8px; padding:20px; background:#f5f5f5;" +
                        "       text-align:center; border-radius:8px;'>" +
                        otp +
                        "  </div>" +
                        "  <p style='color:#888; font-size:12px;'>Valid for 5 days till order delievery | for reset password valid for 10 minutes. Do not share.</p>" +
                        "</div>";

        helper.setText(htmlContent, true); // true = isHtml

        mailSender.send(message);
    }
    public void sendOrderStatusEmail(String toEmail, String name,
                                     String orderId, String status)
            throws MessagingException {

        String subject;
        String statusLine;
        String color;

        switch (status.toUpperCase()) {
            case "ACCEPTED":
                subject = "Order Accepted  #" + orderId;
                statusLine = "Great news! Your order has been accepted by the seller and is being prepared.";
                color = "#4CAF50";
                break;
            case "REJECTED":
                subject = "Order Rejected  #" + orderId;
                statusLine = "Unfortunately your order has been rejected by the seller. Any payment will be refunded.";
                color = "#f44336";
                break;
            case "CANCELLED_BY_USER":
                subject = "Order Cancelled #" + orderId;
                statusLine = "The customer has cancelled order #" + orderId + ". Stock has been restored.";
                color = "#FF9800";
                break;
            case "DELIVERED":
                subject = "Order Delivered  #" + orderId;
                statusLine = "Your order has been successfully delivered! Thank you for shopping with Gruhani.";
                color = "#2196F3";
                break;
            default:
                subject = "Order Update #" + orderId;
                statusLine = "Your order #" + orderId + " status has been updated to: " + status;
                color = "#9C27B0";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("gruhani214@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);

        String htmlContent =
                "<div style='font-family:Arial; max-width:500px; margin:auto;'>" +
                        "  <h2 style='color:" + color + ";'>Gruhani 🛒</h2>" +
                        "  <p>Hi <b>" + name + "</b>,</p>" +
                        "  <p>" + statusLine + "</p>" +
                        "  <div style='padding:16px; background:#f5f5f5; border-radius:8px;" +
                        "       border-left: 4px solid " + color + ";'>" +
                        "    <b>Order ID:</b> #" + orderId +
                        "  </div>" +
                        "  <p style='color:#888; font-size:12px; margin-top:16px;'>" +
                        "  Team Gruhani</p>" +
                        "</div>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
    // In MailService.java
    public void sendForgotPasswordOtp(String toEmail, String name,
                                      String otp) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("gruhani214@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Gruhani - Password Reset OTP 🔐");

        String htmlContent =
                "<div style='font-family:Arial; max-width:500px; margin:auto;'>" +
                        "  <h2 style='color:#e91e63;'>Gruhani 🛒</h2>" +
                        "  <p>Hi <b>" + name + "</b>,</p>" +
                        "  <p>Your OTP to reset your password is:</p>" +
                        "  <div style='font-size:32px; font-weight:bold; color:#333;" +
                        "       letter-spacing:8px; padding:20px; background:#f5f5f5;" +
                        "       text-align:center; border-radius:8px;'>" +
                        otp +
                        "  </div>" +
                        "  <p style='color:#888; font-size:12px; margin-top:16px;'>" +
                        "  Valid for 10 minutes. Do not share with anyone.</p>" +
                        "  <p>Team Gruhani</p>" +
                        "</div>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
    public void sendSellerOrderUpdateEmail(String toEmail, String sellerName,
                                           String orderId, String status)
            throws MessagingException {

        String subject;
        String messageLine;
        String color;

        switch (status.toUpperCase()) {

            case "ACCEPTED":
                subject = "Order Accepted Successfully #" + orderId;
                messageLine = "You have accepted Order #" + orderId +
                        ". Please proceed with preparation and delivery.";
                color = "#4CAF50";
                break;

            case "CANCELLED":
                subject = "Order Cancelled #" + orderId;
                messageLine = "You have cancelled Order #" + orderId +
                        ". The customer has been notified.";
                color = "#f44336";
                break;

            default:
                subject = "Order Update #" + orderId;
                messageLine = "Order #" + orderId + " status updated to: " + status;
                color = "#9C27B0";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("gruhani214@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);

        String htmlContent =
                "<div style='font-family:Arial; max-width:500px; margin:auto;'>" +
                        "<h2 style='color:" + color + ";'>Gruhani Seller Panel 🏪</h2>" +
                        "<p>Hi <b>" + sellerName + "</b>,</p>" +
                        "<p>" + messageLine + "</p>" +
                        "<div style='padding:16px; background:#f5f5f5; border-radius:8px;" +
                        " border-left: 4px solid " + color + ";'>" +
                        "<b>Order ID:</b> #" + orderId +
                        "</div>" +
                        "<p style='color:#888; font-size:12px; margin-top:16px;'>" +
                        "Manage your orders from the dashboard.</p>" +
                        "<p>Team Gruhani</p>" +
                        "</div>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
