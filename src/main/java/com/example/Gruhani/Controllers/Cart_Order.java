package com.example.Gruhani.Controllers;

import com.example.Gruhani.dtos.AddtoCartDto;
import com.example.Gruhani.service.authutil;
import com.example.Gruhani.service.cartService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Cart_Order {

    @Autowired
    authutil at;
    @Autowired
    cartService cartserv;


    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addtocart(@RequestBody AddtoCartDto dto, HttpServletRequest req)
    {
              String header=req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body("Missing or invalid Authorization header");
        }
              String actualtoken=header.substring(7);
              List<Object> l=at.validatetoken(actualtoken);
                 return cartserv.addtocarts(l,dto);



    }






}
