package com.example.Gruhani;

import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.models.userdetails;
import com.example.Gruhani.models.userdetailsServices;
import com.example.Gruhani.service.authutil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class jwtfilter extends OncePerRequestFilter {

    @Autowired
    authutil a;
    @Autowired
    UserRepo ur;
    @Autowired
    userdetailsServices us;

   /* @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // Skip login, register, swagger, static resources etc.
        return path.equals("/logins");
    }*/



    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headauth= request.getHeader("Authorization");
        System.out.println("inside jwt filter");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }


        System.out.print("inside jwt shpuldnt be here");
        String path = request.getServletPath();
        if (path.equals("/logins") ||
                path.equals("/register") ||
                path.equals("/register-seller")) {
            filterChain.doFilter(request, response);
            return;
        }
        if(headauth!=null && headauth.startsWith("Bearer ") )
        {
               String s=headauth.split("Bearer ")[1];
                String user=a.validatetoken(s);//validate and get username from token
                 userdetails u= (userdetails) us.loadUserByUsername(user);
                 if(u!=null && SecurityContextHolder.getContext().getAuthentication()==null)
                 {
                     SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u.getUsername(),null,u.getAuthorities()));
                    // SecurityContextHolder.getContext().setAuthentication(auth);
                     System.out.print(">>> Auth set in SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());
                     logger.debug(">>> Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                     logger.debug(">>> Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

                 }
        }
        filterChain.doFilter(request,response);
    }
}
