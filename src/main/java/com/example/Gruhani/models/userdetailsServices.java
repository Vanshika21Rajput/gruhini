package com.example.Gruhani.models;

import com.example.Gruhani.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class userdetailsServices implements UserDetailsService {
    @Autowired
    UserRepo ur;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user= ur.findByEmail(username).get();
  System.out.println("inside userdetailsservice"+user.getRole());
        return new userdetails(user.getEmail(),user.getPassword(),user.getRole());

    }
}
