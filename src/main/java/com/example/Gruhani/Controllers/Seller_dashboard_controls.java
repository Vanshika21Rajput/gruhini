package com.example.Gruhani.Controllers;

import com.cloudinary.Cloudinary;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.productdto;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.product;
import com.example.Gruhani.service.CloudinaryService;
import com.example.Gruhani.service.addproduct_db;
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
    @Autowired
    SellerRepo sr;
    @Autowired
    ProductRepo prepo;
    @Autowired
    UserRepo urepo;
@Autowired
CloudinaryService cloudinaryService;
    @Autowired
    addproduct_db db;


    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/add-product")
    public ResponseEntity<?> method(@RequestPart("data") productdto pdto ,@RequestPart("image") MultipartFile image) {
        System.out.println("inside add prodict");
        Map<String, Object> response = new HashMap<>();
        ;
        try {
            product pr = new product();
            pr.setDescription(pdto.getDescription());
          String imageurl=cloudinaryService.uploadImage(image);
            pr.setImage(imageurl);
            pr.setPrice(pdto.getPrice());
            pr.setName(pdto.getName());
            pr.setRating(pdto.getRating());
            pr.setStatus("pending");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            System.out.println("pehle instanve ke");
            if (principal instanceof UserDetails) {
                System.out.println("bbad instanve ke" + principal);
                UserDetails user = (UserDetails) principal;
                System.out.println("Username: " + user.getUsername());
                System.out.println("Authorities: " + user.getAuthorities());
                System.out.println("email");
                Seller seller = sr.findByuser_email(user.getUsername());//_ tells spring to move from users in seller table to id in users table
                System.out.println("pseller" + seller);
                pr.setSeller(seller);
            }
            System.out.print("product" + pr);
         //   pr.setId(java.util.UUID.randomUUID().toString());
            pr.setVerified(pdto.isVerified());
            pr.setCategory(pdto.getCategory());
            pr.setSubcategory(pdto.getSubcategory());


            pr.setStatus("pending");
            prepo.save(pr);
        /*    // Create the low-level client builder
            RestClientBuilder builder = RestClient.builder(
                    new HttpHost("localhost", 9200, "http")
            );

// Then create the OpenSearch client from it
            OpenSearchClient client = new OpenSearchClient(
                    new RestClientTransport(
                            builder.build(),
                            new JacksonJsonpMapper()
                    )
            );*/

            //db.addindex(pdto);


            response.put("success", true);
            response.put("message", "Seller registered successfully");


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }


    }

}
