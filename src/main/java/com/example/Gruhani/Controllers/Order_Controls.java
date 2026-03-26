package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.FeedBackDto;
import com.example.Gruhani.dtos.orderReceiveDto;

import com.example.Gruhani.dtos.OrderSellerResponseDto;
import com.example.Gruhani.service.Authutil;
import com.example.Gruhani.service.OrderService;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.substring;

@RestController
public class Order_Controls {
@Autowired
Authutil auth;
@Autowired
    UserRepo userRepo;
@Autowired
OrderService orderService;



    @PostMapping("/place-order")
    public ResponseEntity<OrderSellerResponseDto> placingOrder(@RequestBody orderReceiveDto receiveDto, HttpServletRequest request) throws FirebaseMessagingException {
  //NOTIFICATIONS ARE REMAINING TO BE SENT -user ko otp bhejo and selller ko info ki order aaya hai
       return ResponseEntity.ok().body(orderService.processOrder(receiveDto,request));

    }
    @PostMapping("/cancel-order/{id}")
    public ResponseEntity<?>  cancelOrder(@PathVariable("id")Long id)
    {
        try
        {
            orderService.cancelOrder(id);
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Stock was updated by another user. Please retry.");
        }
        return ResponseEntity.status(200).body(Map.of(
                "success", true,
                "OrderId", id,
                "OrderStatus", "CANCELLED"
        ));
    }
    @GetMapping("/view-order-user")
    public ResponseEntity<?> viewUserOrders(@RequestParam(required = false) String orderStatus)
    {
           List<OrderSellerResponseDto> sellerResponses=orderService.viewOrdersToUser(orderStatus);
        return ResponseEntity.status(200).body(Map.of(
                "success", true,
                "Seller-Details",sellerResponses
        ));
    }
    @GetMapping ("/orders/{orderId}/resend-otp")
    public ResponseEntity<String> resendOtp(@PathVariable("orderId") Long orderId) {
        orderService.resendOtp(orderId);
        return ResponseEntity.ok("OTP resent to your email!");
    }
@PostMapping("/feedback")
    public ResponseEntity<?> feedback(@RequestBody @Valid FeedBackDto feedBackDto)
{
                   orderService.feedback(feedBackDto);
    return ResponseEntity.ok("Feedback submitted successfully");
}

    
}
