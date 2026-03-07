package com.example.Gruhani.service;

import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.Package.ProductNotFoundException;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.models.Product;
import com.example.Gruhani.models.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SellerDashBoardService {
   @Autowired
   usernameFromContext usernameFromContext;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    CloudinaryService cloudinaryService;
    public Long addproduct(ProductDto productDto, MultipartFile image)
    {
        try
        {
            Product product = new Product();

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setSubcategory(productDto.getSubcategory());
            product.setDeliveryTime(productDto.getDeliveryTime());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());

            product.setRating(productDto.getRating());
            product.setDiscount(productDto.getDiscount());
            product.setBadge(productDto.getBadge());

            product.setStatus(ProductStatus.PENDING);
            product.setVerified(false);

            String productImage = cloudinaryService.uploadImage(image);
            product.setImage(productImage);


            Seller seller = sellerRepo.findByuser_email(usernameFromContext.fetchUsername());
            if(seller==null)
            {
                throw new RuntimeException("Seller not found");
            }
            product.setSeller(seller);
            productRepo.save(product);
            return product.getId();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
    public void deleteproduct(Long id)
    {
        if(productRepo.findById(id).get()==null)
        {
            throw new ProductNotFoundException("Product Not Found");
        }
        productRepo.deleteById(id);
        
    }
}

