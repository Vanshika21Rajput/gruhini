package com.example.Gruhani.Controllers;


import com.example.Gruhani.Package.UserNotFoundException;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.sellerDto;
import com.example.Gruhani.dtos.userDto;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.service.usernameFromContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    usernameFromContext usernamefromContext;
 @Autowired
    BCryptPasswordEncoder bcp;

// REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid userDto user) {
        try {
            System.out.println("Register endpoint hit with data: " + user.getName());

            Users u = new Users();

               // Generate unique ID
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
    @Transactional
    public ResponseEntity<Map<String, Object>> sellerRegister( @Valid @RequestBody sellerDto sd)
    {
        System.out.print("inside-sller");
        System.out.print("seller-mail"+sd.getEmail());

      Users us=ur.findByemail(sd.getEmail()).get();
        System.out.print("user-seller"+us.getEmail());

        Seller seller=new Seller();
        seller.setContactNo(sd.getPhone());

        seller.setBusinessName(sd.getBusinessName());
        seller.setApproved(false);
        Set<String> s=new HashSet<>();
        s.add("ROLE_USER");
        s.add("ROLE_SELLER");
            us.setRole(s);
     //   seller.setUser(us);
       // seller.setId(java.util.UUID.randomUUID().toString());
        seller.setCategories(sd.getCategories());

        seller.setUser(us);
        srepo.save(seller);
        ur.save(us);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful as a seller",
                "seller", Map.of(
                        "id", seller.getId(),
                        "name",seller.getUser().getName(),
                        "businessName", seller.getBusinessName()

                )
        ));

    }
    @GetMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id)
    {

        Users user = ur.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Check if you are registered before deleting account")
                );


        ur.deleteById(user.getId());
       return ResponseEntity.ok("Successfully Deleted User Account");

    }


}

