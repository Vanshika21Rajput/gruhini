package com.example.Gruhani.Controllers;
import com.example.Gruhani.Exceptions.UserNotFoundException;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.SellerReceiveDto;
import com.example.Gruhani.dtos.UserDto;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.service.ProfileService;
import com.example.Gruhani.service.UsernameFromContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RegisterALL {
    @Autowired
    SellerRepo srepo;
    @Autowired
    UserRepo ur;
    @Autowired
    UsernameFromContext usernamefromContext;
 @Autowired
    BCryptPasswordEncoder bcp;
@Autowired
ProfileService profileService;
// REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid UserDto user) {
        try {
            System.out.println("Register endpoint hit with data: " + user.getName());

           profileService.registerUser(user);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "User registered successfully");
                response.put("User", user.getName());

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
    public ResponseEntity<Map<String, Object>> sellerRegister( @Valid @RequestBody SellerReceiveDto sellerReceiveDto)
    {
        System.out.print("inside seller");
        profileService.registerSeller(sellerReceiveDto);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful as a seller",
                "seller", Map.of(
                        "name", sellerReceiveDto.getName(),
                        "businessName", sellerReceiveDto.getBusinessName()
        )));

    }
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id)
    {
        Users user = ur.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Check if you are registered before deleting account")
                );
        user.setSeller(null);
        ur.delete(user);
       return ResponseEntity.ok("Successfully Deleted User Account");

    }
}

