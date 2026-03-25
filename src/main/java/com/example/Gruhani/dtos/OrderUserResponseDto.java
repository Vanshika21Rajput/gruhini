package com.example.Gruhani.dtos;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.models.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class OrderUserResponseDto {
    Long id;
    LocalDateTime placedAt;
    BigDecimal orderValue;
    String message;
    UserDetailsDto userDetails;
    OrderStatus orderStatus;
    String deliveryTime;
    AddressDto deliveryAddress;
    List<OrderItemDto> orderItems;


    public OrderUserResponseDto(Long id,BigDecimal ordervalue,LocalDateTime time,String message,UserDetailsDto userDetails,OrderStatus orderStatus,String deliveryTime,AddressDto deliveryAddress,List<OrderItemDto>orderItemDtos)
    {
        this.id=id;
        this.orderValue=ordervalue;
        placedAt=time;
        this.message=message;
        this.userDetails=userDetails;
        this.orderStatus=orderStatus;
        this.deliveryAddress=deliveryAddress;
        this.deliveryTime=deliveryTime;
        this.orderItems=orderItemDtos;

    }
}
