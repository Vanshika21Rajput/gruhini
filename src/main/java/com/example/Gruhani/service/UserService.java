package com.example.Gruhani.service;

import com.example.Gruhani.Exceptions.ProductNotFoundException;
import com.example.Gruhani.Exceptions.UserNotFoundException;
import com.example.Gruhani.Repositories.PasswordResetOtpRepository;
import com.example.Gruhani.Repositories.ProductRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.*;
import com.example.Gruhani.models.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordResetOtpRepository passwordResetOtpRepository;
   @Autowired
    BCryptPasswordEncoder bcp;
    @Autowired
    MailService emailService;
    @Autowired
    ProductRepo productRepo;

    public SellerDetailsDto searchSeller(Long id)
    {
        Seller seller=sellerRepo.findById(id).orElseThrow(()->new UserNotFoundException("SELLER NOT FOUND"));
        SellerDetailsDto sellerDto= maptoSellerDto(seller);
        return sellerDto;
    }



    private SellerDetailsDto maptoSellerDto(Seller seller) {
        SellerDetailsDto sellerDto=new SellerDetailsDto();
        sellerDto.setBusinessName(seller.getBusinessName());
        sellerDto.setContact(seller.getContactNo());
        sellerDto.setName(seller.getUser().getName());
        sellerDto.setAddress(maptoAddressDto(seller.getAddress()));
        sellerDto.setImage(seller.getUser().getProfileImageUrl());
        sellerDto.setId(seller.getId());
        sellerDto.setDescription(sellerDto.getDescription());
        return sellerDto;
    }
    private AddressDto maptoAddressDto(Address address) {
        AddressDto addressDto=new AddressDto();
        addressDto.setAddressLine(address.getAddressLine());
        addressDto.setId(address.getId());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setPincode(address.getPincode());
        return addressDto;
    }
    @Transactional
    public String createOtp(String email) {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("NO USER"));

        passwordResetOtpRepository.deleteByUser(user);

        String otp =  String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1_000_000));


        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setUser(user);
        resetOtp.setOtp(bcp.encode(otp));
        resetOtp.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        resetOtp.setUsed(false);

        passwordResetOtpRepository.save(resetOtp);

        return otp;
    }
    @Transactional
    public void forgotPassword(String email) {

        // Check user exists
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No account found with this email"));

        // Delete any old OTPs for this user
        passwordResetOtpRepository.deleteByUser(user);

        // Generate new 6 digit OTP
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1_000_000));

        // Save OTP in DB
        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setUser(user);
        resetOtp.setOtp(bcp.encode(otp));
        resetOtp.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        resetOtp.setUsed(false);
        passwordResetOtpRepository.save(resetOtp);
        // Send email
        try {
            emailService.sendForgotPasswordOtp(
                    user.getEmail(),
                    user.getName(),
                    otp
            );
        } catch (Exception e) {
            // Clean up OTP if email fails
            passwordResetOtpRepository.deleteByUser(user);
            throw new RuntimeException("Failed to send OTP email, please try again");
        }
    }

    // ─────────────────────────────────────
// STEP 2 — Verify OTP + Reset Password
// ─────────────────────────────────────
    @Transactional
    public void resetPassword(ResetPasswordDto dto) {

        // Find user
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found with this email"));

        // Find valid unused OTP for this user
        PasswordResetOtp resetOtp = passwordResetOtpRepository
                .findByUserAndUsedFalseAndExpiryTimeAfter(user, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("OTP expired or not found, please request a new one"));

        // Verify OTP matches

        if (!bcp.matches(dto.getOtp(), resetOtp.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Mark OTP as used
        resetOtp.setUsed(true);

        // Update password
        user.setPassword(bcp.encode(dto.getNewPassword()));
        userRepo.save(user);
    }

    public List<SellerSummaryDto> getAllSellers() {
        List<Seller>sellers=sellerRepo.findByIsApproved(true);
        List<SellerSummaryDto>sellerSummaryDtos=sellers.stream().map(this::maptoSellerSummaryDto).collect(Collectors.toList());
        return sellerSummaryDtos;
    }

    private SellerSummaryDto maptoSellerSummaryDto(Seller seller) {
        SellerSummaryDto sellerSummaryDto=new SellerSummaryDto();
        sellerSummaryDto.setBusinessName(seller.getBusinessName());
        sellerSummaryDto.setCategory(seller.getCategories());
        sellerSummaryDto.setRating(seller.getRating());
        sellerSummaryDto.setProfileImageUrl(seller.getUser().getProfileImageUrl());
        sellerSummaryDto.setId(seller.getId());
        return  sellerSummaryDto;
    }
    private ProductDto mapToProductDto(Product product) {
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

        dto.setSellerId(product.getSeller().getId());
        return dto;
    }
    public ProductDto viewSingleProduct(Long id)
    {
        Product product=productRepo.findById(id).orElseThrow(()->new ProductNotFoundException("NOT FOUND"));
        ProductDto  productdto=mapToProductDto(product);
        return productdto;
    }
}
