package com.example.Gruhani.service;

import com.example.Gruhani.Repositories.CartItemRepository;
import com.example.Gruhani.Repositories.CartRepo;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.AddtoCartDto;
import com.example.Gruhani.models.Cart;
import com.example.Gruhani.models.CartItem;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.models.product;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class cartService {

    @Autowired
    ProductRepo pr;
    @Autowired
    UserRepo ur;
    @Autowired
    CartRepo cr;
    @Autowired
    CartItemRepository cartItemRepository;
    @Transactional
    public ResponseEntity<String> addtocarts(List<Object> l,  AddtoCartDto addtocart)
    {
               Long u_id= (Long) l.get(2);
               Cart cart;
               Cart cart1=cr.findByu_id(u_id);
        Users user=ur.findById(u_id).orElseThrow(()->new RuntimeException("no  user found"));
               if(cart1==null)
               {
                   cart=new Cart();
                   cart.setL(new ArrayList<>());
                   cart.setU(user);
                   cr.save(cart);

               }
               else {
                   cart=cart1;
               }

               cart.setUpdadtedAt(LocalDateTime.now());

        Optional<CartItem> existingItem =
                cartItemRepository.findByC_idAndP_id(cart.getId(), addtocart.getProductid());

        CartItem toaddedinCart;
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            toaddedinCart=item;

        } else {

                product pduct = pr.findByid(addtocart.getProductid()).orElseThrow(()->new RuntimeException("no product found"));
            System.out.println("Product ID from request = " + addtocart.getProductid());

            toaddedinCart=new CartItem();
                toaddedinCart.setC(cart);
                toaddedinCart.setP(pduct);
                toaddedinCart.setPriceAtAddTime(pduct.getPrice());
                toaddedinCart.setQuantity(addtocart.getQuantity());
                cart.getL().add(toaddedinCart);



        }
        cr.save(cart);


return ResponseEntity.ok("product successfully added to plate !");




    }



}
