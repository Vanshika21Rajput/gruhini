package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.dtos.OrderUserResponseDto;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.dtos.SellerAdminDetailsDto;
import com.example.Gruhani.dtos.SellerDetailsDto;
import com.example.Gruhani.models.SelectedItemsByAdmin;
import com.example.Gruhani.models.SellerOrderSummary;
import com.example.Gruhani.service.AdminService;
import com.example.Gruhani.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//ADD SELLER APPROVE ENDPOINT AS WELL
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    SellerRepo sr;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    AdminService adminService;
    @Autowired
    OrderService orderService;

    //PENDING REQUESTS FOR PRODUCT APPROVAL
    @GetMapping("/view-pending")
    public ResponseEntity<?> viewPending() {
        List<ProductDto> productdtos = adminService.viewPending();
        return ResponseEntity.ok().body(productdtos);
    }


    @PostMapping("/accept-item")
    public ResponseEntity<String> acceptItem(@RequestBody SelectedItemsByAdmin selected) {
        if (selected == null) {
            return ResponseEntity.badRequest().body("Could not Process the request");
        }
        adminService.acceptItem(selected);
        return ResponseEntity.ok().body("APPROVED YOUR PRODUCT");
    }

    @PostMapping("/reject-item")
    public ResponseEntity<String> rejectItem(@RequestBody SelectedItemsByAdmin selected) {
        if (selected == null) {
            return ResponseEntity.badRequest().body("No products selected");
        }
        adminService.rejectItem(selected);
        return ResponseEntity.ok().body("PRODUCT REJECTED BECAUSE OF" + selected.getMessage());
    }
    //VIEW ORDER BY SELLER group by selller ids so that admin can see the stats



    @GetMapping("/products-viewAll")
    public ResponseEntity<?> viewAll() {
        List<ProductDto> productDto = adminService.viewAllProducts();
        return ResponseEntity.ok().body(productDto);
    }

    @GetMapping("/Sellers-viewAll")
    public ResponseEntity<?> viewAllSellers() {
        List<SellerDetailsDto> sellerDetailsDtos = adminService.searchAllSeller();
        return ResponseEntity.ok().body(sellerDetailsDtos);
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProducts(@RequestBody SelectedItemsByAdmin selectedItemsByAdmin) {
        if (selectedItemsByAdmin == null) {
            return ResponseEntity.badRequest().body("NO PRODUCTS SELECTED");
        }
        adminService.deleteProducts(selectedItemsByAdmin.getSelectedProducts());
        return ResponseEntity.ok("SUCCESSFULLY DELETED THE SELECTED PRODUCTS");

    }

    @DeleteMapping("/delete-seller/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable("id") Long id) {
        adminService.deleteSeller(id);
        return ResponseEntity.ok("SUCCESSFULLY DELETED THE SELLER");

    }

    @GetMapping("/view-seller/{id}")
    public ResponseEntity<?> viewSeller(@PathVariable("id") Long id) {
        SellerAdminDetailsDto sellerDetailsDto = adminService.searchSeller(id);
        return ResponseEntity.ok(sellerDetailsDto);//view their order as well as revenue

    }

    @GetMapping("/view-orders")
    public ResponseEntity<?> viewOrders(@RequestParam(required = false) String orderStatus) {
        List<SellerOrderSummary> sellerOrderSummaries = adminService.getSellerOrderSummary(orderStatus);
        return ResponseEntity.ok(sellerOrderSummaries);
    }

    @GetMapping("/view-orders/seller/{id}")
    public ResponseEntity<?> viewOrders(@PathVariable("id") Long id, @RequestParam(required = false) String Status) {
        List<OrderUserResponseDto> sellerOrders = orderService.viewSellerOrderToAdmin(id, Status);
        return ResponseEntity.ok(sellerOrders);
    }

    @PostMapping("/approve-seller")
    public ResponseEntity<?> approveSeller(@RequestBody SelectedItemsByAdmin selectedItemsByAdmin) {
        adminService.approveSeller(selectedItemsByAdmin);
        return ResponseEntity.ok("APPROVED SELLER");
    }

    @GetMapping("/pending-seller")
    public ResponseEntity<?> approveSeller() {

        List<SellerDetailsDto>l=adminService.viewPendingSeller();
        return ResponseEntity.ok(l);
    }
}




