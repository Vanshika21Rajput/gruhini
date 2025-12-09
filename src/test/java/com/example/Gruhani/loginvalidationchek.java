package com.example.Gruhani;

import com.example.Gruhani.Controllers.AuthController;
import com.example.Gruhani.Controllers.JwtController;
import com.example.Gruhani.dtos.LoginRequest;
import com.example.Gruhani.dtos.userDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class loginvalidationchek {
    @Autowired
    JwtController auth;


   @Test
    public void method()
   {
       userDto d=new userDto();
       d.setEmail("vanshika@example.com");
       d.setContact("9131206200");
       d.setName("vanshika");
       d.setPassword("1234567890");
       //Assertions.assertEquals();
   }



}
