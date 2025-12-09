package com.example.Gruhani.service;

import com.example.Gruhani.models.Users;
import com.example.Gruhani.models.userdetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class authutil {
    @Value("${jwt.secretkey}")
    String skey;

    public SecretKey getskey()
    {
        return Keys.hmacShaKeyFor(skey.getBytes(StandardCharsets.UTF_8));
    }

    Map<String ,Object>mp=new HashMap<>();

    public String generateToken(userdetails u)
    {
        return Jwts.builder().setSubject(u.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+10*60*1000))
                .signWith(getskey())
                .compact();

    }

    public String validatetoken(String headauth) {
        Claims c=Jwts.parser().
                verifyWith(getskey()).build().parseSignedClaims(headauth).getPayload();
        return c.getSubject();
    }
}
