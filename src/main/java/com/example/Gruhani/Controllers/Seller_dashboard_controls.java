package com.example.Gruhani.Controllers;

import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.*;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Product;
import com.example.Gruhani.service.CloudinaryService;
import com.example.Gruhani.service.OrderService;
import com.example.Gruhani.service.ProfileService;
import com.example.Gruhani.service.SellerDashBoardService;

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
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(("/seller"))
@PreAuthorize("hasRole('SELLER')")
public class Seller_dashboard_controls {

    @Autowired
    SellerDashBoardService sellerDashBoardService;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    OrderService orderService;
    @Autowired
    ProfileService profileService;


    @PostMapping(value = "/add-product",consumes = "multipart/form-data")
    public ResponseEntity<?> method(@RequestPart("data") ProductReceiveDto pdto , @RequestPart("image") MultipartFile image) {
        System.out.println("inside add product");
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
    @GetMapping("/get-All-products")
    public ResponseEntity<?>getProduct(@RequestParam(required = false) String productStatus)
    {
        List<ProductDto> productDtoList=sellerDashBoardService.getAllProducts(productStatus);
        return ResponseEntity.ok(productDtoList);

    }
    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteproduct(@RequestParam("id") Long id)
    {
        Map<String, Object> response = new HashMap<>();

          sellerDashBoardService.deleteproduct(id);
            response.put("message","product deleted successfully");
            response.put("success", true);
            return ResponseEntity.ok(response);
    }
    @PatchMapping("/update-product")
    public ResponseEntity<?>updateProduct(@RequestBody ProductDto productDto)
    {
        sellerDashBoardService.updateProduct(productDto);
        return ResponseEntity.ok("PRODUCT UPDATED SUCCESSFULLY");
    }

   @PostMapping("/accept-order")
           public ResponseEntity<?> acceptOrder(@RequestBody List<Long>orderIds)
   {
               orderService.acceptOrder(orderIds);
               return ResponseEntity.ok("ORDER ACCEPTED");

   }
    @PostMapping("/reject-order")
    public ResponseEntity<?> rejectOrder(@RequestBody List<Long>orderIds)
    {
        orderService.rejectOrder(orderIds);
        return ResponseEntity.ok("ORDER REJECTED");

    }
    @GetMapping("/view-order-seller")
    public ResponseEntity<?> viewSellerOrders(@RequestParam(required = false)String orderStatus)
    {
        List<OrderUserResponseDto> orderUserResponseDtos=orderService.viewOrderToSeller(orderStatus);
        return ResponseEntity.status(200).body(Map.of(
                "success", true,
                "User details",orderUserResponseDtos
        ));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam("orderId")Long id,@RequestParam("otp")String otp)
    {
           if(orderService.verifyOtp(id,otp))
           {
               return ResponseEntity.ok("OTP Verified");
           }
           return ResponseEntity.ok("OTP NOT VERIFIED ENTER CORRET ONE");
    }
    @PostMapping("/update-profile")
   public ResponseEntity<?> updateSeller(@RequestBody SellerReceiveDto sellerReceiveDto)
    {
      profileService.updateSellerProfile(sellerReceiveDto);
        return ResponseEntity.ok("UPDATED PROFILE SUCCESFULLY");
    }
    @GetMapping("/get-seller-profile")
    public ResponseEntity<?> getSellerProfile()
    {
      SellerDetailsDto sellerDetailsDto= profileService.getSellerProfile();
        return ResponseEntity.ok(sellerDetailsDto);
    }





}
