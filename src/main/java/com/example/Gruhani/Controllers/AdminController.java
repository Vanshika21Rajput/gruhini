package com.example.Gruhani.Controllers;

import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.Package.ProductNotFoundException;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.models.Idclass;
import com.example.Gruhani.models.SelectedProductsbyAdmin;
import com.example.Gruhani.models.Product;
import com.example.Gruhani.service.addproduct_db;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    SellerRepo sr;
    @Autowired
    ProductRepo productRepo;


    //PENDING REQUESTS FOR PRODUCT APPROVAL
    @GetMapping("/view-pending")
    public ResponseEntity<?> view_pending() {

        List<Product> l = productRepo.findAllBystatus(ProductStatus.PENDING);

        List<ProductDto>productdtos= l.stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    BeanUtils.copyProperties(product, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(productdtos);
    }


    @PostMapping("/accept-item")
    public ResponseEntity<String> accept_item(@RequestBody SelectedProductsbyAdmin selected) {
        List<Long> selectedProducts = selected.getSelectedProducts();
        productRepo.batchUpdateStatus(ProductStatus.APPROVED,selectedProducts);


        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/reject-item")
    public ResponseEntity<String> reject_item(@RequestBody SelectedProductsbyAdmin sb) {
        List<Long> selectedProducts = sb.getSelectedProducts();
           productRepo.batchUpdateStatus(ProductStatus.REJECTED,selectedProducts);

        return ResponseEntity.ok().body("ok");
    }
    //VIEW ORDER BY SELLER group by selller ids so that admin can see the stats

    @GetMapping("/products/{id}")
    public ResponseEntity<?> View_single_product(@PathVariable("id")Long id)
    {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
                   ProductDto dto = new ProductDto();
                    BeanUtils.copyProperties(product, dto);
        return ResponseEntity.ok(dto);
    }
    }



