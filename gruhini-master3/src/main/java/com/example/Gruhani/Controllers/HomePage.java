package com.example.Gruhani.Controllers;




import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.dtos.productdto;
import com.example.Gruhani.models.product;



import com.example.Gruhani.service.addproduct_db;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HomePage {
    @Autowired
    SellerRepo sr;
    @Autowired
    ProductRepo prepo;

    @Autowired
    addproduct_db db;

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
        List<product> l = prepo.findAllBystatus("approved");
        
        // Map products with seller info for frontend
        List<Map<String, Object>> response = l.stream()
                .map(product -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", product.getId());
                    dto.put("name", product.getName());
                    dto.put("title", product.getName()); // Frontend uses 'title'
                    dto.put("price", product.getPrice());
                    dto.put("category", product.getCategory());
                    dto.put("description", product.getDescription());
                    dto.put("image", product.getImage());
                    dto.put("img", product.getImage()); // Frontend uses 'img'
                    dto.put("verified", product.getVerified());
                    dto.put("rating", product.getRating());
                    dto.put("stock", product.getStock());
                    
                    // Add seller/chef info for frontend compatibility
                    if (product.getSeller() != null) {
                        dto.put("chef", product.getSeller().getBusinessName());
                        dto.put("loc", product.getSeller().getUser() != null ? "Home Kitchen" : "");
                        
                        Map<String, Object> sellerInfo = new HashMap<>();
                        sellerInfo.put("businessName", product.getSeller().getBusinessName());
                        sellerInfo.put("id", product.getSeller().getId());
                        dto.put("seller", sellerInfo);
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
                
        return ResponseEntity.ok().body(response);
    }




}







