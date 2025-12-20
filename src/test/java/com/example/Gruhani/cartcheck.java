package com.example.Gruhani;

import com.example.Gruhani.Controllers.Cart_Order;
import com.example.Gruhani.service.cartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class cartcheck {
    @Autowired
    Cart_Order corder;
    @Autowired
    cartService ser;

    @Test
    public void test()
    {


    }
}
