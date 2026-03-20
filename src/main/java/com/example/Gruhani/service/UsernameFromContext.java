package com.example.Gruhani.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UsernameFromContext {
    public  String fetchUsername()
    {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) auth.getPrincipal();
        String username=userDetails.getUsername();
        return  username;

    }
}
