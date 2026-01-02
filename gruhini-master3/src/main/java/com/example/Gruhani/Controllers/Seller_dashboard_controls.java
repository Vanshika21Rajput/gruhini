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
}
