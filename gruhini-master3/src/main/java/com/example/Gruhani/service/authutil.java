package com.example.Gruhani.service;

import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.models.userdetails;
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
public class authutil {
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

        Users user=ur.findByemail(u.getUsername());
        Map<String ,Object>mp=new HashMap<>();
        mp.put("roles", u.getAuthorities());
        mp.put("user_id",user.getId());
        List<String> roles = u.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder().setSubject(u.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .claims(mp)
                .expiration(new Date(System.currentTimeMillis()+10*60*60*1000))
                .signWith(getskey())
                .compact();

    }

    public List<Object> validatetoken(String headauth) {
        Claims c=Jwts.parser().
                verifyWith(getskey()).build().parseSignedClaims(headauth).getPayload();

        List<String>roles=c.get("roles",List.class);
        List<Object>l=new ArrayList<>();
        l.add(c.getSubject());
        l.add(roles);
        l.add(c.get("user_id",Long.class));
        return l ;
    }
}
