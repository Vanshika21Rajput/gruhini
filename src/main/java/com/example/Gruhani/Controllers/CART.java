package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.CartRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.AddtoCartDto;
import com.example.Gruhani.models.Cart;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.service.cartService;
import com.example.Gruhani.service.UsernameFromContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CART {

    @Autowired
    cartService cartserv;
    @Autowired
    CartRepo cartRepo;
    @Autowired
    UsernameFromContext usernameFromContext;
    @Autowired
    UserRepo userRepo;



    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addtocart(@RequestBody AddtoCartDto dto)
    {
             String username= usernameFromContext.fetchUsername();
                              Users u=userRepo.findByEmail(username).get();
                 return cartserv.addtocarts(u.getId(),dto);

    }
    @GetMapping("/get-cart")
    public ResponseEntity<?> getcart(HttpServletRequest req)
    {
        List<Map<String, Object>> items = new ArrayList<>();
      try {
          String username = usernameFromContext.fetchUsername();
          Cart cart = cartserv.getCartbyUsername(username);
          for (var cartItem : cart.getCartItems()) {
              if (cartItem.getProduct() != null) {
                  Map<String, Object> item = new HashMap<>();
                  item.put("productid", cartItem.getProduct().getId());
                  item.put("productname", cartItem.getProduct().getName());
                  item.put("chefname", cartItem.getProduct().getSeller().getUser().getName());
                  item.put("price", cartItem.getPriceAtAddTime());
                  item.put("image", cartItem.getProduct().getImage());
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
                          Users user=userRepo.findByEmail(username).orElseThrow(()->new RuntimeException("user not found"));

        Cart cart = cartRepo.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));


        boolean removed = cart.getCartItems().removeIf(
                item -> item.getProduct().getId().equals(id)//removal of cartitem from cart makes it orphan and hence is auto deleted by JPA in DB
        );

        if (!removed) {
            throw new RuntimeException("Cart item not found");
        }
        cartRepo.save(cart);
        return ResponseEntity.ok("Product Deleted Successfully");
    }



//QUANTITY UPDET KARNE K BAAD HOW TO SAVE IT TO DB



}
