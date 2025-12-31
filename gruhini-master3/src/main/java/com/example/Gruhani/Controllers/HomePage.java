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
        Map<String, Object> response = new HashMap<>();
        //here in response the product dto attributes will be mapped and sent to frontend
        List<productdto>s= l.stream()
                .map(product -> {
                    productdto dto = new productdto();
                    BeanUtils.copyProperties(product, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(s);

    }




}







