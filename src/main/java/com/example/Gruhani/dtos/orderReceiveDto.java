package com.example.Gruhani.dtos;

import com.example.Gruhani.models.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class orderReceiveDto {
    List<CartItem> cartItemList;
    String deliveryAddress;

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
