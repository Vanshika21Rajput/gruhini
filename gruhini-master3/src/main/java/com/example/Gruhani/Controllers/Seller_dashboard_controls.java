package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.dtos.productdto;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Seller_dashboard_controls {

    @Autowired SellerRepo sr;
    @Autowired ProductRepo prepo;

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping(value = "/add-product", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addProduct(
            @RequestPart("data") String productData,  // JSON String
            @RequestPart(value = "image", required = false) MultipartFile image // File
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 1. Convert JSON String to DTO
            ObjectMapper mapper = new ObjectMapper();
            productdto pdto = mapper.readValue(productData, productdto.class);

            // 2. Handle Image Upload (Cloudinary Logic would go here)
            // For now, if an image URL is passed in DTO, use it. 
            // If a file is uploaded, you would upload to Cloudinary and set the URL.
            String imageUrl = pdto.getImage(); 
            if (image != null && !image.isEmpty()) {
                // String cloudUrl = cloudinaryService.upload(image);
                // imageUrl = cloudUrl;
                System.out.println("Image file received: " + image.getOriginalFilename());
            }

            // 3. Create Product Entity
            product pr = new product();
            pr.setName(pdto.getName());
            pr.setDescription(pdto.getDescription());
            pr.setPrice(pdto.getPrice());
            pr.setCategory(pdto.getCategory());
            pr.setSubcategory(pdto.getSubcategory());
            pr.setStock(pdto.getStock());
            pr.setImage(imageUrl); 
            pr.setRating(4.5); // Default
            pr.setVerified(true);
            pr.setStatus("APPROVED");

            // 4. Link to Seller
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails user = (UserDetails) auth.getPrincipal();
            Seller seller = sr.findByuser_email(user.getUsername());
            pr.setSeller(seller);

            prepo.save(pr);

            response.put("success", true);
            response.put("message", "Product Added Successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // GET seller's dishes for dashboard
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller/dishes")
    public ResponseEntity<?> getSellerDishes() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails user = (UserDetails) auth.getPrincipal();
            Seller seller = sr.findByuser_email(user.getUsername());
            
            if (seller == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Seller not found"));
            }

            // Get all products for this seller
            java.util.List<product> dishes = prepo.findAllBySeller(seller);
            
            // Convert to response format
            java.util.List<Map<String, Object>> dishList = dishes.stream().map(p -> {
                Map<String, Object> dish = new HashMap<>();
                dish.put("id", p.getId());
                dish.put("name", p.getName());
                dish.put("price", p.getPrice());
                dish.put("category", p.getCategory());
                dish.put("image", p.getImage());
                dish.put("available", "APPROVED".equalsIgnoreCase(p.getStatus()));
                dish.put("rating", p.getRating());
                return dish;
            }).collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(dishList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // GET seller stats for dashboard
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller/stats")
    public ResponseEntity<?> getSellerStats() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails user = (UserDetails) auth.getPrincipal();
            Seller seller = sr.findByuser_email(user.getUsername());
            
            if (seller == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Seller not found"));
            }

            java.util.List<product> dishes = prepo.findAllBySeller(seller);

            Map<String, Object> stats = new HashMap<>();
            stats.put("activeDishes", dishes.size());
            stats.put("todayEarning", "₹0"); // Would need Order model
            stats.put("todayOrders", 0);
            stats.put("weeklyEarning", "₹0");
            stats.put("monthlyEarning", "₹0");
            stats.put("rating", 4.8);
            stats.put("repeatCustomers", "67%");
            stats.put("kitchenOpen", true);
            stats.put("sellerName", seller.getBusinessName());
            
            // NEW: Include profile fields
            stats.put("bio", seller.getBio() != null ? seller.getBio() : "");
            stats.put("yearsExperience", seller.getYearsExperience() != null ? seller.getYearsExperience() : 0);
            stats.put("avatar", seller.getAvatar() != null ? seller.getAvatar() : "");
            stats.put("location", seller.getLocation() != null ? seller.getLocation() : "Home Kitchen");

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // GET all sellers for public listing
    @GetMapping("/sellers")
    public ResponseEntity<?> getAllSellers() {
        try {
            java.util.List<Seller> sellers = sr.findAll();
            
            java.util.List<Map<String, Object>> sellerList = sellers.stream().map(s -> {
                Map<String, Object> seller = new HashMap<>();
                seller.put("id", s.getId());
                seller.put("name", s.getBusinessName());
                seller.put("bio", s.getBio() != null ? s.getBio() : "घर का खाना, प्यार से बना");
                seller.put("yearsExperience", s.getYearsExperience() != null ? s.getYearsExperience() : 1);
                seller.put("avatar", s.getAvatar());
                seller.put("location", s.getLocation() != null ? s.getLocation() : "Home Kitchen");
                seller.put("categories", s.getCategories());
                seller.put("isApproved", s.getApproved());
                
                // Get dish count
                java.util.List<product> dishes = prepo.findAllBySeller(s);
                seller.put("dishCount", dishes.size());
                
                return seller;
            }).collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(sellerList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
