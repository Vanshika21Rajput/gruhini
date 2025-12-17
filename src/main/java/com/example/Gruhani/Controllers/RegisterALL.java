package com.example.Gruhani.Controllers;


import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.sellerDto;
import com.example.Gruhani.dtos.userDto;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Users;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
public class RegisterALL {
    @Autowired
    SellerRepo srepo;
    @Autowired
    UserRepo ur;

@Autowired
    BCryptPasswordEncoder bcp;

// REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid userDto user) {
        try {
            System.out.println("Register endpoint hit with data: " + user.getName());

            Users u = new Users();

                u.setid(java.util.UUID.randomUUID().toString()); // Generate unique ID
                u.setEmail(user.getEmail());
                Set<String> r = new HashSet<>();
                r.add("ROLE_USER");
                u.setRole(r);
                u.setName(user.getName());
                u.setPassword(bcp.encode(user.getPassword()));
                u.setContact(user.getContact());


                ur.save(u);


                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "User registered successfully");
                response.put("UserId", u.getId());

                return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }

    }



    // REGISTER SELLER

    @PostMapping("/register-seller")
    public ResponseEntity<?> sellerRegister( @Valid @RequestBody sellerDto sd)
    {
        System.out.print("inside-sller");
        System.out.print("seller-mail"+sd.getEmail());

      /*  Users us=ur.findByemail(sd.getEmail());
        System.out.print("user-seller"+us.getEmail());

        if(us==null)
        {
            return ResponseEntity.notFound().build();
        }*/
        Seller seller=new Seller();
        seller.setContactNo(sd.getPhone());
        seller.setName(sd.getName());
        seller.setBusinessName(sd.getBusinessName());
        seller.setApproved(false);
        Set<String> s=new HashSet<>();
        s.add("ROLE_USER");
        s.add("ROLE_SELLER");
    //    us.setRole(s);
     //   seller.setUser(us);
        seller.setId(java.util.UUID.randomUUID().toString());
        seller.setCategories(sd.getCategories());
        seller.setEmail(sd.getEmail());
        Users usu=seller.getUser();
        srepo.save(seller);
        System.out.print("final seller"+seller.getEmail());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful as a seller",
                "seller", Map.of(
                        "id", seller.getId(),
                        "name", seller.getName(),
                        "businessName", seller.getBusinessName(),
                        "email", seller.getEmail()
                )
        ));

    }


}

