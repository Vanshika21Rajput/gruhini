package com.example.Gruhani.Controllers;

import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Product;
import com.example.Gruhani.service.CloudinaryService;
import com.example.Gruhani.service.SellerDashBoardService;
import com.example.Gruhani.service.addproduct_db;
import org.checkerframework.checker.units.qual.A;
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


@RestController("/seller")
public class Seller_dashboard_controls {

    @Autowired
    SellerDashBoardService sellerDashBoardService;
    @Autowired
    ProductRepo productRepo;


    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/add-product")
    public ResponseEntity<?> method(@RequestPart("data") ProductDto pdto , @RequestPart("image") MultipartFile image) {
        System.out.println("inside add prodict");
        Map<String, Object> response = new HashMap<>();

        try {
             Long product_id= sellerDashBoardService.addproduct(pdto,image);
            response.put("success", true);
            response.put("message", "Seller registered successfully");
            response.put("product_id",product_id);


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }


    }
    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteproduct(@RequestParam Long id)
    {
        Map<String, Object> response = new HashMap<>();

          sellerDashBoardService.deleteproduct(id);
            response.put("message","product deleted successfully");
            response.put("success", true);
            return ResponseEntity.ok(response);
    }
    //uodate the product enpot mut be added

}
