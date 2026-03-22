package com.example.Gruhani.dtos;

import com.example.Gruhani.models.Address;
import com.example.Gruhani.models.CartItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
public class orderReceiveDto {
    private Long addressId;

}
