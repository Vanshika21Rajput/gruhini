package com.example.Gruhani.service;



import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;


public class NotificationService {
    // Add this method inside your existing NotificationService.java

    public void sendOtpNotification(String fcmToken, String otp, String orderId)
            throws FirebaseMessagingException {

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle("Gruhani Order OTP 🔐")
                        .setBody("Your OTP for order #" + orderId + " is: " + otp + ". Valid for 10 minutes.")
                        .build())
                .putData("otp", otp)               // also send as data for frontend to read
                .putData("orderId", orderId)
                .putData("type", "ORDER_OTP")      // so frontend knows what kind of notification
                .build();

        FirebaseMessaging.getInstance().send(message);
        System.out.println("OTP notification sent for order: " + orderId);
    }
    // src/main/java/com/gruhani/service/NotificationService.java




        public void sendOrderNotification(String fcmToken, String orderStatus, String orderId)
                throws FirebaseMessagingException {

            // Set title & body based on order status
            String title;
            String body;

            switch (orderStatus.toUpperCase()) {
                case "CONFIRMED":
                    title = "Order Confirmed! ";
                    body  = "Your order #" + orderId + " has been confirmed.";
                    break;
                case "REJECTED":
                    title = "Order Rejected By seller ";
                    body  = "Your order #" + orderId + " is being cancelled by seller.";
                    break;
                case "ACCEPTED":
                    title = "Order Accepted by Seller ";
                    body  = "Your order #" + orderId + " is on the way!";
                    break;
                case "DELIVERED":
                    title = "Order Delivered ";
                    body  = "Your order #" + orderId + " has been delivered. Enjoy!";
                    break;
                case "CANCELLED":
                    title = "Order Cancelled ";
                    body  = "Your order #" + orderId + " was cancelled.";
                    break;
                default:
                    title = "Order Update";
                    body  = "Your order #" + orderId + " status: " + orderStatus;
            }

            Message message = Message.builder()
                    .setToken(fcmToken)           // user's device token
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("orderId", orderId)
                    .putData("status", orderStatus)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Notification sent: " + response);
        }
    }

