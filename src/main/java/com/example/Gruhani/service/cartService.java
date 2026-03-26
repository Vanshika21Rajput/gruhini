package com.example.Gruhani.service;

import com.example.Gruhani.Exceptions.InvalidCart;
import com.example.Gruhani.Exceptions.ProductNotFoundException;
import com.example.Gruhani.Repositories.CartItemRepository;
import com.example.Gruhani.Repositories.CartRepo;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.AddtoCartDto;
import com.example.Gruhani.models.Cart;
import com.example.Gruhani.models.CartItem;
import com.example.Gruhani.models.Users;
import com.example.Gruhani.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
@Service
public class cartService {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    UserRepo ur;
    @Autowired
    CartRepo cartRepo;
    @Autowired
    CartItemRepository cartItemRepository;
    @Transactional
    public ResponseEntity<String> addtocarts(Long userid, AddtoCartDto addtocart) {

        Users user = ur.findById(userid)
                .orElseThrow(() -> new RuntimeException("No user found"));

        Cart cart = cartRepo.findByUser_Id(userid)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepo.save(newCart);
                });

        Product product = productRepo.findById(addtocart.getProductid())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if(!cart.getCartItems().isEmpty()) {
            Long existingSellerId = cart.getCartItems()
                    .get(0)
                    .getProduct()
                    .getSeller()
                    .getId();

            Long newSellerId = product.getSeller().getId();

            if (!existingSellerId.equals(newSellerId)) {
                return ResponseEntity.badRequest()
                        .body("Cart can only contain items from one seller.");
            }
        }

        Optional<CartItem> existingItem =
                cartItemRepository. findByCartIdAndProductId(cart.getId(), addtocart.getProductid());

        if (existingItem.isPresent()) {

            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + addtocart.getQuantity());

        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setPriceAtAddTime(product.getPrice());
            cartItem.setQuantity(addtocart.getQuantity());
            cart.getCartItems().add(cartItem);
        }
        cartRepo.save(cart);
        return ResponseEntity.ok("Product successfully added to cart");
    }


    public Cart getCartbyUsername(String username) {
        return cartRepo.findCartWithItems(username).orElseThrow(()->new InvalidCart("Cart Doesn't Exist"));
    }
}
