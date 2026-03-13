package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.orderReceiveDto;
import com.example.Gruhani.dtos.orderResponseDto;
import com.example.Gruhani.models.CartItem;
import com.example.Gruhani.models.Order;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.service.authutil;
import com.example.Gruhani.service.orderService;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.substring;

@RestController
public class Order_Controls {
@Autowired
    authutil auth;
@Autowired
    UserRepo userRepo;
@Autowired
orderService orderService;



    @PostMapping("/place-order")
    public ResponseEntity<orderResponseDto> placingOrder(@RequestBody orderReceiveDto receiveDto, HttpServletRequest request)
    {
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


    
}
