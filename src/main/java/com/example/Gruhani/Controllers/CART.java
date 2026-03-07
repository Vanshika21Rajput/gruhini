package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.CartRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.AddtoCartDto;
import com.example.Gruhani.models.Cart;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.service.authutil;
import com.example.Gruhani.service.cartService;
import com.example.Gruhani.service.usernameFromContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CART {

    @Autowired
    authutil at;
    @Autowired
    cartService cartserv;
    @Autowired
    CartRepo cartRepo;
    @Autowired
    usernameFromContext usernameFromContext;
    @Autowired
    UserRepo userRepo;



    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addtocart(@RequestBody AddtoCartDto dto)
    {
             String username= usernameFromContext.fetchUsername();
                              Users u=userRepo.findByemail(username).get();
                 return cartserv.addtocarts(u.getId(),dto);

    }
    @GetMapping("/get-cart")
    public ResponseEntity<?> getcart(HttpServletRequest req)
    {
        List<Map<String, Object>> items = new ArrayList<>();
      try {
          String username = usernameFromContext.fetchUsername();
          Cart cart = cartserv.getCartbyUsername(username);
          for (var cartItem : cart.getL()) {
              if (cartItem.getP() != null) {
                  Map<String, Object> item = new HashMap<>();
                  item.put("productid", cartItem.getP().getId());
                  item.put("productname", cartItem.getP().getName());
                  item.put("chefname", cartItem.getP().getSeller().getUser().getName());
                  item.put("price", cartItem.getPriceAtAddTime());
                  item.put("image", cartItem.getP().getImage());
                  item.put("quantity", cartItem.getQuantity());
                  items.add(item);
              }
          }
      }

        catch (Exception e) { e.printStackTrace(); return ResponseEntity.status(500).body(Map.of("message", "Error: " + e.getMessage())); }
           
        return ResponseEntity.ok(items);



    }
    @DeleteMapping ("/remove-from-cart/{id}")
    @Transactional
    public ResponseEntity<String> deleteCartItem(@PathVariable("id")Long id) {

        String username= usernameFromContext.fetchUsername();
                          Users user=userRepo.findByemail(username).orElseThrow(()->new RuntimeException("user not found"));

        Cart cart = cartRepo.findByu_id(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        boolean removed = cart.getL().removeIf(
                item -> item.getId().equals(id)//removal of cartitem from cart makes it orphan and hence is auto deleted by JPA in DB
        );

        if (!removed) {
            throw new RuntimeException("Cart item not found");
        }

        cart.setUpdadtedAt(LocalDateTime.now());
        cartRepo.save(cart);
        return ResponseEntity.ok("Product Deleted Successfully");
    }







}
