package com.example.Gruhani.service;

import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.models.JwtClaims;
import com.example.Gruhani.models.userdetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class Authutil {
    @Autowired
    UserRepo ur;
    @Value("${jwt.secretkey}")
    String skey;

    public SecretKey getskey()
    {
        return Keys.hmacShaKeyFor(skey.getBytes(StandardCharsets.UTF_8));
    }



    public String generateToken(userdetails u)
    {
        Users user = ur.findByEmail(u.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + u.getUsername()));

        List<String> roles = u.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        JwtClaims jwtClaims=new JwtClaims();
        jwtClaims.setRoles(roles);
        jwtClaims.setUserid(user.getId());
        Map<String,Object>mp=new HashMap<>();
        mp.put("jwtClaims",jwtClaims );
        return Jwts.builder()
                .claims(mp)
                .subject(u.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000 * 60))
                .signWith(getskey())
                .compact();


    }

    public JwtClaims validatetoken(String headauth) {
        Claims c=Jwts.parser().
                verifyWith(getskey()).build().parseSignedClaims(headauth).getPayload();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(c.get("jwtClaims"), JwtClaims.class);


    }
}
