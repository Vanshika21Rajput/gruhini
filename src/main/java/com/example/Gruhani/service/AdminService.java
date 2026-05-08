package com.example.Gruhani.service;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.Exceptions.InvalidOrder;
import com.example.Gruhani.Exceptions.ProductNotFoundException;
import com.example.Gruhani.Exceptions.UserNotFoundException;
import com.example.Gruhani.Repositories.OrderRepository;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.dtos.AddressDto;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.dtos.SellerAdminDetailsDto;
import com.example.Gruhani.dtos.SellerDetailsDto;
import com.example.Gruhani.models.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    .map(this::mapToProductDto)
                    .collect(Collectors.toList());
            return productDtos;
    }

    private ProductDto mapToProductDto(Product product) {
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
        // quantity has no matching field in Product — set default or remove from DTO

        dto.setSellerId(product.getSeller().getId());
        return dto;
    }

    //we have not used transactional because in db for this query is already annotated wit transactional
    public void acceptItem(SelectedItemsByAdmin selectedItemsByAdmin)
    {
        List<Long> selectedProducts = selectedItemsByAdmin.getSelectedProducts();
        productRepo.batchUpdateStatus(ProductStatus.APPROVED,selectedProducts, selectedItemsByAdmin.getMessage());

    }
    public void rejectItem(SelectedItemsByAdmin selectedItemsByAdmin)
    {
        List<Long> selectedProducts = selectedItemsByAdmin.getSelectedProducts();
        productRepo.batchUpdateStatus(ProductStatus.REJECTED,selectedProducts, selectedItemsByAdmin.getMessage());
    }

    public List<ProductDto> viewAllProducts() {
        List<Product>productList=productRepo.findAll();
        List<ProductDto>products=productList.stream().map(this::mapToProductDto).collect(Collectors.toList());
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
    public SellerAdminDetailsDto searchSeller(Long id)
    {
        Seller seller=sellerRepo.findById(id).orElseThrow(()->new UserNotFoundException("SELLER NOT FOUND"));
                       SellerAdminDetailsDto sellerDetailsDto= maptoSellerAdminDetailsDto(seller);
                       return sellerDetailsDto;
    }

    private SellerAdminDetailsDto maptoSellerAdminDetailsDto(Seller seller) {
        SellerAdminDetailsDto sellerAdminDetailsDto=new SellerAdminDetailsDto();
         sellerAdminDetailsDto.setSellerName(seller.getUser().getName());
         sellerAdminDetailsDto.setSellerId(seller.getId());
         sellerAdminDetailsDto.setImage(seller.getUser().getProfileImageUrl());
         sellerAdminDetailsDto.setContact(seller.getContactNo());
         sellerAdminDetailsDto.setBusinessName(seller.getBusinessName());
         sellerAdminDetailsDto.setIsApproved(seller.getIsApproved());
        sellerAdminDetailsDto.setTotalOrders(orderRepository.countBySeller_id(seller.getId()));

        sellerAdminDetailsDto.setDeliveredOrders(
                orderRepository.countBySeller_IdAndOrderStatus(seller.getId(), OrderStatus.DELIVERED)
        );


        sellerAdminDetailsDto.setPendingOrders(
                orderRepository.countBySeller_IdAndOrderStatus(seller.getId(), OrderStatus.PENDING)
        );

        sellerAdminDetailsDto.setTotalRevenue(
                orderRepository.sumRevenueBySellerId(seller.getId())
        );
        return sellerAdminDetailsDto;
    }

    private AddressDto maptoAddressDto(Address address) {
        AddressDto addressDto=new AddressDto();
        addressDto.setAddressLine(address.getAddressLine());
        addressDto.setId(address.getId());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setPincode(address.getPincode());
        return  addressDto;
    }
    private SellerDetailsDto maptoSellerDto(Seller seller) {
        SellerDetailsDto sellerDto=new SellerDetailsDto();
        sellerDto.setBusinessName(seller.getBusinessName());
        sellerDto.setContact(seller.getContactNo());
        sellerDto.setName(seller.getUser().getName());
        sellerDto.setAddress(maptoAddressDto(seller.getAddress()));
        sellerDto.setImage(seller.getUser().getProfileImageUrl());
        sellerDto.setId(seller.getId());
        sellerDto.setDescription(seller.getDescription());
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

    @Transactional
    public void approveSeller(SelectedItemsByAdmin selectedItemsByAdmin) {

           List<Seller>sellers= sellerRepo.findAllById(selectedItemsByAdmin.getSelectedProducts());
           for(Seller seller:sellers)
           {
               seller.setIsApproved(true);
           }

    }
    @Transactional
    public  List<SellerDetailsDto> viewPendingSeller() {

        List<Seller>sellers= sellerRepo.findByIsApproved(false);
        List<SellerDetailsDto>sellerDtos=sellers.stream().map(this::maptoSellerDto).collect(Collectors.toList());
        return sellerDtos;

    }
}
