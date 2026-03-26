package com.example.Gruhani.service;

import com.example.Gruhani.Enums.Role;
import com.example.Gruhani.Exceptions.UserNotFoundException;
import com.example.Gruhani.Repositories.AddressRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.*;
import com.example.Gruhani.models.Address;
import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProfileService {
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    BCryptPasswordEncoder bcp;
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    UsernameFromContext usernameFromContext;

    @Transactional
    public void registerSeller(SellerReceiveDto sellerReceiveDto)
    {
        Users user=userRepo.findByEmail(sellerReceiveDto.getEmail()).orElseThrow(()->new UserNotFoundException("REGISTER AS A USER FIRST"));
       // System.out.print("user-seller"+user.getEmail());
        if (sellerRepo.existsByUser(user)) {
            throw new RuntimeException("Seller already exists");
        }
        Seller seller=new Seller();
        seller.setContactNo(sellerReceiveDto.getPhone());
        seller.setBusinessName(sellerReceiveDto.getBusinessName());
        seller.setIsApproved(false);
      //  Set<Role> s=new HashSet<>();
        user.getRole().add(Role.ROLE_SELLER);
       // user.setRole(s);
         seller.setUser(user);
        seller.setCategories(sellerReceiveDto.getCategories());
        seller.setUser(user);
        seller.setDescription(sellerReceiveDto.getDescription());
        Address address = new Address();
        address.setAddressLine(sellerReceiveDto.getAddressDto().getAddressLine());
        address.setCity(sellerReceiveDto.getAddressDto().getCity());
        address.setState(sellerReceiveDto.getAddressDto().getState());
        address.setPincode(sellerReceiveDto.getAddressDto().getPincode());
        address.setUser(user);
        seller.setAddress(address);
        sellerRepo.save(seller);


    }
    @Transactional
    public void registerUser(UserDto user)
    {
        Users u = new Users();
        u.setEmail(user.getEmail());
        Set<Role> r = new HashSet<>();
        r.add(Role.ROLE_USER);
        u.setRole(r);
        u.setName(user.getName());
        u.setPassword(bcp.encode(user.getPassword()));
        u.setContact(user.getContact());
        userRepo.save(u);
        Address address=mapToAddress(u,user.getAddressDto());
        addressRepo.save(address);
        List<Address> l=new ArrayList<>();
        l.add(address);
        u.setAddresses(l);

    }

    private Address mapToAddress(Users u, AddressDto addressDto) {
        Address address=new Address();
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPincode(addressDto.getPincode());
        address.setUser(u);
       return address;

    }
    @Transactional
    public void updateProfile(UserDto userDto)
    {
        Users user=userRepo.findByEmail(usernameFromContext.fetchUsername()).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));
        user.setContact(userDto.getContact());
        user.setName(userDto.getName());
        if (userDto.getPassword() != null) {
            user.setPassword(bcp.encode(userDto.getPassword()));
        }
    }
    @Transactional
    public void updateAddress(AddressDto addressDto)
    {
        Users user=userRepo.findByEmail(usernameFromContext.fetchUsername()).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));
        List<Address>addressList=user.getAddresses();
         Address address=mapToAddress(user,addressDto);
         addressList.add(address);
         user.setAddresses(addressList);

    }
    @Transactional
    public void updateProfilePicture(MultipartFile file) throws IOException {
        String username= usernameFromContext.fetchUsername();
        Users user=userRepo.findByEmail(username).orElseThrow(()->new UserNotFoundException("NO USER FOUND"));
        String imageUrl=cloudinaryService.uploadImage(file);
        if(user.getProfileImageUrl()!=null) {
            cloudinaryService.deleteImage(user.getProfileImageUrl());
        }
        user.setProfileImageUrl(imageUrl);
    }

    public UserProfileDto viewProfile() {
       String username=usernameFromContext.fetchUsername();
       Users user=userRepo.findByEmail(username).orElseThrow(()->new UserNotFoundException("NO USER"));
      return mapToProfileDto(user);
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

    private UserProfileDto mapToProfileDto(Users user) {
        UserProfileDto userProfileDto=new UserProfileDto();
        userProfileDto.setAddresses(
                user.getAddresses() == null ? List.of() :
                        user.getAddresses()
                                .stream()
                                .map(this::maptoAddressDto)
                                .toList()
        );
        userProfileDto.setProfileImageUrl(user.getProfileImageUrl());
        userProfileDto.setId(user.getId());
        userProfileDto.setName(user.getName());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setContact(user.getContact());
        return userProfileDto;

    }
    public SellerDetailsDto getSellerProfile() {
        String email = usernameFromContext.fetchUsername();

        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));

        Seller seller = sellerRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("SELLER NOT FOUND"));

        return maptoSellerDto(seller);
    }
    private SellerDetailsDto maptoSellerDto(Seller seller) {
        SellerDetailsDto sellerDto = new SellerDetailsDto();
        sellerDto.setId(seller.getId());
        sellerDto.setName(seller.getUser().getName());
        sellerDto.setBusinessName(seller.getBusinessName());
        sellerDto.setContact(seller.getContactNo());
        sellerDto.setImage(seller.getUser().getProfileImageUrl());
        sellerDto.setAddress(seller.getAddress() != null ? maptoAddressDto(seller.getAddress()) : null);
        return sellerDto;
    }
    @Transactional
    public void updateSellerProfile(SellerReceiveDto sellerReceiveDto) {
        String email = usernameFromContext.fetchUsername();

        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));

        Seller seller = sellerRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("SELLER NOT FOUND"));

        // Update seller fields
        seller.setContactNo(sellerReceiveDto.getPhone());
        seller.setBusinessName(sellerReceiveDto.getBusinessName());
        seller.setCategories(sellerReceiveDto.getCategories());
        seller.setDescription(sellerReceiveDto.getDescription());

        // Update address
        Address address = seller.getAddress();
        if (address == null) {
            address = new Address();
            address.setUser(user);
        }
        address.setAddressLine(sellerReceiveDto.getAddressDto().getAddressLine());
        address.setCity(sellerReceiveDto.getAddressDto().getCity());
        address.setState(sellerReceiveDto.getAddressDto().getState());
        address.setPincode(sellerReceiveDto.getAddressDto().getPincode());
        seller.setAddress(address);

        // Update name on user if provided
        if (sellerReceiveDto.getName() != null) {
            user.setName(sellerReceiveDto.getName());
        }
    }

}
