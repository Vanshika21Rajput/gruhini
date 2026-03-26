package com.example.Gruhani.Controllers;

import com.example.Gruhani.dtos.LoginRequest;
import com.example.Gruhani.models.userdetails;
import com.example.Gruhani.service.Authutil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthUsingJWT {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    Authutil at;

    @PostMapping("/logins")
    public ResponseEntity<Map<String, String>> method(@RequestBody @Valid LoginRequest loginRequest)
    {
        Authentication auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
         if(auth.isAuthenticated())
         {
            userdetails ut= (userdetails) auth.getPrincipal();
            String token=at.generateToken(ut);
            return ResponseEntity.ok(Map.of("token", token));
         }
         return (ResponseEntity<Map<String, String>>) ResponseEntity.badRequest();

    }

}
