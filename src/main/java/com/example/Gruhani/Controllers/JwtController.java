package com.example.Gruhani.Controllers;

import com.example.Gruhani.dtos.userDto;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.models.userdetails;
import com.example.Gruhani.service.authutil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    authutil at;

    @PostMapping("/logins")
    public ResponseEntity<String> method(@RequestBody @Valid userDto u)
    {
        Authentication auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(u.getEmail(),u.getPassword()));
         if(auth.isAuthenticated())
         {
            userdetails ut= (userdetails) auth.getPrincipal();
            String token=at.generateToken(ut);
            return ResponseEntity.ok(token);
         }
         return ResponseEntity.badRequest().body("try again later dear");

    }
}
