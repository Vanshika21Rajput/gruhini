package com.example.Gruhani.Controllers;

import com.example.Gruhani.dtos.LoginDto;
import com.example.Gruhani.models.userdetails;
import com.example.Gruhani.service.authutil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class JwtController {

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    authutil at;

    @PostMapping("/logins")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginDto loginDto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            
            if (auth.isAuthenticated()) {
                userdetails ut = (userdetails) auth.getPrincipal();
                String token = at.generateToken(ut);
                
                // Get roles as comma-separated string
                String roles = ut.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
                
                // Check if user is seller
                boolean isSeller = roles.contains("ROLE_SELLER");
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("token", token);
                response.put("role", isSeller ? "SELLER" : "CUSTOMER");
                response.put("roles", roles);
                response.put("name", ut.getUsername());
                response.put("message", "Login successful");
                
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "Invalid credentials"
            ));
            
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "Invalid email or password"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Login failed: " + e.getMessage()
            ));
        }
    }
}
