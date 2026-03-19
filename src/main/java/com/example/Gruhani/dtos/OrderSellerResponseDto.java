package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.OrderStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class orderResponseDto {
    Long id;
    LocalDateTime placedAt;
    BigDecimal price;
    String message;
    SellerDetailsDto sellerDetailsDto;
    OrderStatus orderStatus;

    public orderResponseDto(Long id,BigDecimal ordervalue,LocalDateTime time,String message,SellerDetailsDto sellerDetailsDto,OrderStatus orderStatus)
    {
        this.id=id;
        price=ordervalue;
        placedAt=time;
        this.message=message;
        this.sellerDetailsDto=sellerDetailsDto;
        this.orderStatus=orderStatus;

    }

}
