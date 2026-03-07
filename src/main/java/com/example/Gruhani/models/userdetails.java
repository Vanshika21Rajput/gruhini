package com.example.Gruhani.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class userdetails implements UserDetails {
   private String username;
   private String pass;
    Set<String>s;

    public userdetails(String user, String pass, Set<String>r)
    {
        this.username=user;
        this.pass=pass;
        this.s=r;
        System.out.print("userdetails only ke ander");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = s.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        System.out.print("userdetails only"+authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        System.out.print("userdetails only"+username);
        return username;
    }
}
