package com.example.Gruhani.service;

import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.Exceptions.ProductNotFoundException;
import com.example.Gruhani.Exceptions.UserNotFoundException;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.dtos.ProductReceiveDto;
import com.example.Gruhani.dtos.SellerDetailsDto;
import com.example.Gruhani.dtos.SellerReceiveDto;
import com.example.Gruhani.models.Address;
import com.example.Gruhani.models.Product;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerDashBoardService {
   @Autowired
   UsernameFromContext usernameFromContext;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    UserRepo userRepo;
    public Long addproduct(ProductReceiveDto productDto, MultipartFile image)
    {
        try
        {
            Seller seller = sellerRepo.findByuser_email(usernameFromContext.fetchUsername());
            if(seller==null)
            {
                throw new RuntimeException("Seller not found");
            }
            if (!seller.getIsApproved()) {
                throw new RuntimeException("Seller not approved yet");
            }
            Product product = new Product();

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCategory(productDto.getCategory());
            product.setSubcategory(productDto.getSubcategory());
            product.setDeliveryTime(productDto.getDeliveryTime());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            product.setRating(0.0);
            product.setDiscount(productDto.getDiscount());
            product.setBadge(null);
            product.setStatus(ProductStatus.PENDING);
            product.setVerified(false);
            String productImage = cloudinaryService.uploadImage(image);
            product.setImage(productImage);
            product.setSeller(seller);
            productRepo.save(product);
            return product.getId();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @CacheEvict(value="product",key="#id")
    public void deleteproduct(Long id)
    {
       Product product=productRepo.findById(id).orElseThrow(()->new ProductNotFoundException("No Product Found"));
        productRepo.delete(product);
        
    }

    @Transactional
    @Cacheable(value = "allproducts")
    public  List<ProductDto> getAllProducts(String productStatus) {
        String username=usernameFromContext.fetchUsername();
        Seller seller=sellerRepo.findByuser_email(username);
        if(seller==null)
        {
            throw new UserNotFoundException("SELLER NOT FOUND");
        }
        List<Product> productList=new ArrayList<>();
        if(productStatus==null){
            productList=productRepo.findBySellerId(seller.getId());
        }
        else {
            try {
                productList = productRepo.findBySellerIdAndStatus(
                        seller.getId(),
                        ProductStatus.valueOf(productStatus.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ProductNotFoundException("Invalid status: " + productStatus);
            }
        }
        List<ProductDto>productDtos= productList.stream().map(this::MapToSendDto).collect(Collectors.toList());
                         return productDtos;
    }

    private ProductDto MapToSendDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategories(product.getCategory());
        dto.setSubcategory(product.getSubcategory());
        dto.setDescription(product.getDescription());
        dto.setStock(product.getStock());
        dto.setStatus(product.getStatus());
        dto.setRating(product.getRating());
        dto.setDiscount(product.getDiscount());
        dto.setVerified(product.getVerified());
        dto.setDeliveryTime(product.getDeliveryTime());
        dto.setBadge(product.getBadge());
        dto.setMessage(product.getMessage());
        dto.setImage(product.getImage());
        dto.setSellerId(product.getSeller().getId());
        return dto;
    }

    @Transactional
    public void updateProduct(ProductDto dto) {
        if(dto==null)
        {
            throw new ProductNotFoundException("NO PRODUCT SENT!");
        }

        Long id=dto.getId();
        Product product= productRepo.findById(id).orElseThrow(()->new ProductNotFoundException("NO PRODCUT FOUND"));
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getCategories() != null) product.setCategory(dto.getCategories());
        if (dto.getSubcategory() != null) product.setSubcategory(dto.getSubcategory());
        if (dto.getStock() > 0) product.setStock(dto.getStock());
        if (dto.getDiscount() != null) product.setDiscount(dto.getDiscount());
        if (dto.getDeliveryTime() != null) product.setDeliveryTime(dto.getDeliveryTime());
        if (dto.getImage() != null) product.setImage(dto.getImage());
        product.setVerified(false);
        product.setStatus(ProductStatus.PENDING);


    }



}

