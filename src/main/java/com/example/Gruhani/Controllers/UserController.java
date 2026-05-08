package com.example.Gruhani.Controllers;




import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.dtos.ResetPasswordDto;
import com.example.Gruhani.dtos.SellerDetailsDto;
import com.example.Gruhani.dtos.SellerSummaryDto;
import com.example.Gruhani.models.Product;


import com.example.Gruhani.models.Users;
import com.example.Gruhani.service.UserService;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    SellerRepo sr;
    @Autowired
    ProductRepo prepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;


    //SENDING PRODUCTS TO FRONTEND VIA PROTOBUF BUT JS PROTOBUF DECODER FAILED,PROTOBUF SUCCESSFULLY WORKED IN BACKED

/*
    @GetMapping(value = "/get-all-products00", produces = "application/x-protobuf")
    public byte[] getProductsAsProtobufss() {
        List<product> dbProducts = prepo.findAllBystatus("approved");

        ProductOuterClass.ProductList.Builder listBuilder = ProductOuterClass.ProductList.newBuilder();

        for (product p : dbProducts) {
            ProductOuterClass.Producto protoProduct = ProductOuterClass.Producto.newBuilder()
                    .setId(p.getId())
                    .setName(p.getName())
                    .setDescription(p.getDescription())
                    .setPrice(p.getPrice())
                    .setRating(p.getRating())

                    .setVerified(p.getVerified() == null ? false : p.getVerified())
                    .setBadge(p.getBadge())

                    .setCategory(p.getCategory() == null ? "Home Decor" : p.getCategory())
                    .setSubcategory(p.getSubcategory() == null ? "Herbal Soaps" : p.getSubcategory())
                    .setStock(p.getStock())
                    .setImage(p.getImage())
                    .build();

            listBuilder.addProducts(protoProduct);
        }
        return listBuilder.build().toByteArray();
    }

*/
    @GetMapping("/explore")
    public ResponseEntity<?> method() {
        List<ProductDto>productDtos=userService.getAllProducts();
        return ResponseEntity.ok().body(productDtos);
    }
    @PostMapping("/save-fcm-token")
    public ResponseEntity<String> saveFcmToken(
            @RequestParam Long userId,
            @RequestBody Map<String, String> body) {

        String fcmToken = body.get("fcmToken");

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFcmToken(fcmToken);
        userRepo.save(user);

        return ResponseEntity.ok("FCM token saved successfully");
    }
   @GetMapping("/get-allSellers")
   public ResponseEntity<?> getAllSellers()
   {

       List<SellerSummaryDto>sellerSummaryDtos= userService.getAllSellers();
        return ResponseEntity.ok(sellerSummaryDtos);
   }
    @GetMapping("/get-seller/{id}")
    public ResponseEntity<?> sellerDetailsForUser(@PathVariable("id")Long id)
    {
       SellerDetailsDto sellerDetailsDto= userService.searchSeller(id);
       return ResponseEntity.ok(sellerDetailsDto);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgetPassword(@RequestParam String email)
    {
        userService.forgotPassword(email);
        return ResponseEntity.ok("OTP SENT TO YOUR MAIL KINDLY VERIFY AND RESET PASSWORD");
    }
    @PostMapping("/verify-otp-forgetPassword")
    public ResponseEntity<?> verifyOtp(@RequestBody ResetPasswordDto resetPasswordDto)
    {
        userService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok("New Password is Set Successfully");
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<?> viewSingleProduct(@PathVariable("id") Long id) {
        System.out.println("ijndide priduct");
        ProductDto productDto = userService.viewSingleProduct(id);
        return ResponseEntity.ok().body(productDto);
    }
    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/clear-cache")
    public String clearCache() {
        cacheManager.getCache("searchAllSellers").clear();
        return "Cache cleared";
    }
}







