package com.example.Gruhani.service;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.Repositories.OrderRepository;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.OrderItem;
import com.example.Gruhani.dtos.orderReceiveDto;
import com.example.Gruhani.dtos.orderResponseDto;
import com.example.Gruhani.models.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

@Service
public class orderService {
    @Autowired
    authutil auth;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    OrderRepository orderRepository;




    public  List<OrderItem>  MaptoOrderItem(List<CartItem>cartItemList,Order order)
    {
        List<OrderItem>orderItemList=new ArrayList<>();
        for(CartItem i:cartItemList)
        {
            OrderItem orderItem=new OrderItem();

            orderItem.setP(i.getP());
            orderItem.setQuantity(i.getQuantity());
            orderItem.setPriceAtOrderTime(i.getPriceAtAddTime());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        }
        return orderItemList;

    }
@Transactional
    public orderResponseDto processOrder(orderReceiveDto receiveDto, HttpServletRequest req)
    {
        Order order=new Order();
        List<OrderItem> list=MaptoOrderItem(receiveDto.getCartItemList(),order);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty");
        }
        String s=req.getHeader("Authorization");
        String jwt=s.substring(7);
       jwtClaims jwtclaims=auth.validatetoken(jwt);

        Optional<Users> users= Optional.ofNullable(userRepo.findById(jwtclaims.getUser_id()).orElseThrow(() -> new RuntimeException("User Not Found")));
        order.setUser(users.get());
        order.setDeliveryTime("3-4 Days");
        order.setDeliveryAddress(receiveDto.getDeliveryAddress());
        int otp = ThreadLocalRandom.current().nextInt(100000, 1_000_000);

        order.setOtp(otp);

        order.setOrderItemList(list);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setMessage("Order placed Successfully");
        order.setPlacedAt(LocalDateTime.now());
        order.setOrderValue(calculateOrderValue(receiveDto.getCartItemList()));
         decreaseStock(list);
        order.setSeller(list.get(0).getP().getSeller());
        orderRepository.save(order);
return  new orderResponseDto(order.getId(),order.getOrderValue(),order.getPlacedAt(),order.getMessage());


    }

    private void decreaseStock(List<OrderItem> list) {
        for (OrderItem orderItem : list) {
            product p=orderItem.getP();
             p.setStock(p.getStock()-orderItem.getQuantity());
        }
    }

    private boolean validateStock(product p,int quantity)
{
    product product1=productRepo.findById(p.getId()).get();
    if(quantity>product1.getStock())
    {
return false;
    }
    return true;
}
    private BigInteger calculateOrderValue(List<CartItem> cartItemList) {
        BigInteger total= BigInteger.valueOf(0);
        for(CartItem i:cartItemList)
        {
            product p=productRepo.findById(i.getP().getId()).get();
            if(validateStock(p,i.getQuantity()))
            {
                BigInteger price = p.getPrice();
                int quantity = i.getQuantity();
                BigInteger lineTotal = price.multiply(BigInteger.valueOf(quantity));
                total=total.add(lineTotal);

            }
            else {
                throw new RuntimeException("Insufficient Stock,Check Stock availability of"+i.getP().getName());
            }
        }
        return total;
    }
}
