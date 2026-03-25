package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.models.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderSellerResponseDto {
    Long id;
    LocalDateTime placedAt;
    BigDecimal orderValue;
    String message;
    SellerDetailsDto sellerDetails;
    OrderStatus orderStatus;
    String deliveryTime;
    AddressDto deliveryAddress;
    List<OrderItemDto> orderItems;
    private String otpFallback;
    private String otpMessage;
    private boolean emailSent;

    public OrderSellerResponseDto(Long id, BigDecimal ordervalue, LocalDateTime time, String message, SellerDetailsDto sellerDetailsDto, OrderStatus orderStatus, String deliveryTime, AddressDto deliveryAddress, List<OrderItemDto>orderItemDtos,boolean emailSent)
    {
        this.id=id;
        this.orderValue=ordervalue;
        placedAt=time;
        this.message=message;
        this.sellerDetails=sellerDetailsDto;
        this.orderStatus=orderStatus;
        this.deliveryAddress=deliveryAddress;
        this.deliveryTime=deliveryTime;
        this.orderItems=orderItemDtos;
        this.emailSent=emailSent;

    }

}
