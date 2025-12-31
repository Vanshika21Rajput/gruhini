package com.example.Gruhani.dtos;

import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Component;

@Component
public class AddtoCartDto {
    Long productid;
    @Min(1)
    int quantity;


    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Long getProductid() {
        return productid;
    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
