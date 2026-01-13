package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SessionBasedAuth {

    @Autowired
     AuthenticationManager authenticationManager;
    @Autowired
      UserRepo ur;
    @Autowired
    SellerRepo srepo;



    @GetMapping("/home")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Map<String, Object> user = new HashMap<>();
            user.put("id", "1");
            user.put("name", auth.getName());
            user.put("email", auth.getName());
            user.put("type", "customer");
            
            response.put("authenticated", true);
            response.put("user", user);
            response.put("message", "Welcome to Gruhani!");
        } else {
            response.put("authenticated", false);
            response.put("message", "Please login to continue");
        }
        
        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate(); // Invalidate session
        return ResponseEntity.ok("Logged out successfully");
    }


    //SESSION BASED FORM LOGIN LOGIC commented because now we have updated with JWT
  /*  @PostMapping("/logins")
    public ResponseEntity<?> loginPage(@RequestBody @Valid LoginRequest lr, HttpServletRequest req) {
        Authentication authentication;
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(lr.getUsername(), lr.getPassword());
             authentication = authenticationManager.authenticate(auth);

            Set<String> userRoles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            // Size-based check
            System.out.print("final rollaa"+userRoles);
            if (lr.getUserType().equals("seller") && userRoles.size() != 2) {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Invalid email or password"
                ));
            }


         //   req.getSession(true);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
            System.out.print(req.getRequestedSessionId());
            SecurityContextHolder.setContext(context);
            req.getSession(true).setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context
            );
            System.out.print("authentication: aayayai yai " + authentication); // Will print only if success
        } catch (Exception e) {
            System.out.println(" Exception during authentication: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace
        }

        Users user=ur.findByemail(lr.getUsername());
        if(user==null )
        {
            System.out.print("iske anderrrr");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful",
                "user", Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                )
        ));


        // Redirect to frontend login
    }*/
}