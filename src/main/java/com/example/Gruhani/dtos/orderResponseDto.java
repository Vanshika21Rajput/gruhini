package com.example.Gruhani.dtos;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class orderResponseDto {
    Long id;
    LocalDateTime placedAt;
    BigInteger price;
    String message;

    public orderResponseDto(Long id,BigInteger ordervalue,LocalDateTime time,String message)
    {
        this.id=id;
        price=ordervalue;
        placedAt=time;
        this.message=message;

    }

}
