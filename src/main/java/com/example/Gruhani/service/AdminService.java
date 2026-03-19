package com.example.Gruhani.service;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.Package.InvalidOrder;
import com.example.Gruhani.Package.ProductNotFoundException;
import com.example.Gruhani.Package.UserNotFoundException;
import com.example.Gruhani.Repositories.OrderRepository;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.dtos.SellerDetailsDto;
import com.example.Gruhani.models.Product;
import com.example.Gruhani.models.SelectedProductsbyAdmin;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.SellerOrderSummary;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    OrderRepository orderRepository;

    public List<ProductDto> viewPending()
    {

            List<Product> l = productRepo.findAllByStatus(ProductStatus.PENDING);
            List<ProductDto> productDtos = l.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return productDtos;



    }

    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setSubcategory(product.getSubcategory());
        dto.setDescription(product.getDescription());
        dto.setStock(product.getStock());
        dto.setStatus(product.getStatus());
        dto.setRating(product.getRating());
        dto.setDiscount(product.getDiscount());
        dto.setVerified(product.getVerified());
        dto.setDeliveryTime(product.getDeliveryTime());
        dto.setBadge(product.getBadge());
        // quantity has no matching field in Product — set default or remove from DTO
        dto.setQuantity(0);
        dto.setSellerid(product.getSeller().getId());
        return dto;
    }
    public ProductDto viewSingleProduct(Long id)
    {

            Product product=productRepo.findById(id).orElseThrow(()->new ProductNotFoundException("NOT FOUND"));
                           ProductDto  productdto=mapToDto(product);
                           return productdto;
    }
    //we have not used transactional because in db for this query is already annotated wit transactional
    public void acceptItem(SelectedProductsbyAdmin selectedProductsbyAdmin)
    {
        List<Long> selectedProducts = selectedProductsbyAdmin.getSelectedProducts();
        productRepo.batchUpdateStatus(ProductStatus.APPROVED,selectedProducts,selectedProductsbyAdmin.getMessage());

    }
    public void rejectItem(SelectedProductsbyAdmin selectedProductsbyAdmin)
    {
        List<Long> selectedProducts = selectedProductsbyAdmin.getSelectedProducts();
        productRepo.batchUpdateStatus(ProductStatus.REJECTED,selectedProducts,selectedProductsbyAdmin.getMessage());
    }

    public List<ProductDto> viewAllProducts() {
        List<Product>productList=productRepo.findAll();
        List<ProductDto>products=productList.stream().map(this::mapToDto).collect(Collectors.toList());
        return products;
    }

    @Transactional
    public void deleteProducts(List<Long> selectedProducts) {
        List<Product> products = productRepo.findAllById(selectedProducts);
        if (products.size() != selectedProducts.size()) {
            throw new ProductNotFoundException("Some products not found");
        }

            productRepo.deleteAll(products);


    }

    @Transactional
    public void deleteSeller(Long id) {
        Seller seller=sellerRepo.findById(id).orElseThrow(()->new UserNotFoundException("SELLER NOT FOUND"));
                      sellerRepo.delete(seller);
    }
    public SellerDetailsDto searchSeller(Long id)
    {
        Seller seller=sellerRepo.findById(id).orElseThrow(()->new UserNotFoundException("SELLER NOT FOUND"));
                       SellerDetailsDto sellerDetailsDto= maptoSellerDto(seller);
                       return sellerDetailsDto;
    }

    private SellerDetailsDto maptoSellerDto(Seller seller) {
        SellerDetailsDto sellerDto=new SellerDetailsDto();
        sellerDto.setBusinessName(seller.getBusinessName());
        sellerDto.setContact(seller.getContactNo());
        sellerDto.setName(seller.getUser().getName());
        sellerDto.setAddress(seller.getAddress());
        sellerDto.setImage(seller.getUser().getProfileImageUrl());
        return sellerDto;
    }

    public List<SellerDetailsDto> searchAllSeller() {

            return sellerRepo.findAll()
                    .stream()
                    .map(this::maptoSellerDto)
                    .collect(Collectors.toList());

    }
    public List<SellerOrderSummary> getSellerOrderSummary(String orderStatus) {
        if (orderStatus == null) {
            return orderRepository.getOrderCountForSeller(); // existing query
        }
        try {
            return orderRepository.getOrderCountForSellerByStatus(
                    OrderStatus.valueOf(orderStatus.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidOrder("Invalid status: " + orderStatus);
        }
    }

}
