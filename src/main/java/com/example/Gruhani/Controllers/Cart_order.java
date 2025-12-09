/*package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.CartRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.productdto;

import com.example.Gruhani.models.Users;
import com.example.Gruhani.models.cart;
import com.example.Gruhani.models.product;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@RestController
public class Cart_order {
    @Autowired
    UserRepo ur;
    @Autowired
    CartRepo cr;


    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addtocart(@RequestBody product pdto)
    {

       String u= SecurityContextHolder.getContext().getAuthentication().getName();
       Users user=ur.findByemail(u);
       if(user==null)
       {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or not authenticated");
       }
        Optional<cart> crt = CartRepo.findByU(user);
       if(crt.isPresent())
       {
           cart C=crt.get();
           Optional<product> existingProduct = C.getL().stream()
                   .filter(p -> p.getId().equals(pdto.getId()))
                   .findFirst();

           if (existingProduct.isPresent()) {
               existingProduct.get().setQuantity(existingProduct.get().getQuantity() + pdto.getQuantity());
           } else {
               C.getL().add(pdto);
           }

           cr.save(C);
       }
       else {
           cart C=new cart();
           ArrayList<product>l=new ArrayList<>();
           C.setL(l);
           C.getL().add(pdto);
           C.setCartid(java.util.UUID.randomUUID().toString());

           C.setU(user);
           cr.save(C);
       }
return ResponseEntity.ok().body("successful");
    }
}
*/