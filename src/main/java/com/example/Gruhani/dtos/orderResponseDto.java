package com.example.Gruhani.dtos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class orderResponseDto {
    Long id;
    LocalDateTime placedAt;
    BigDecimal price;
    String message;
    SellerDetailsDto sellerDetailsDto;

    public orderResponseDto(Long id,BigDecimal ordervalue,LocalDateTime time,String message,SellerDetailsDto sellerDetailsDto)
    {
        this.id=id;
        price=ordervalue;
        placedAt=time;
        this.message=message;
        this.sellerDetailsDto=sellerDetailsDto;

    }

}
