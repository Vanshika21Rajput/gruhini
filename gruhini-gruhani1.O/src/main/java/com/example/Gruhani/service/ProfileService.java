package com.example.Gruhani.service;

import com.example.Gruhani.Enums.Role;
import com.example.Gruhani.Package.UserNotFoundException;
import com.example.Gruhani.Repositories.AddressRepo;
import com.example.Gruhani.Repositories.SellerRepo;
import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.AddressDto;
import com.example.Gruhani.dtos.SellerReceiveDto;
import com.example.Gruhani.dtos.UserDto;
import com.example.Gruhani.dtos.UserProfileDto;
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
import java.util.stream.Collectors;

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
        Users user=userRepo.findByemail(sellerReceiveDto.getEmail()).orElseThrow(()->new UserNotFoundException("REGISTER AS A USER FIRST"));
        System.out.print("user-seller"+user.getEmail());

        Seller seller=new Seller();
        seller.setContactNo(sellerReceiveDto.getPhone());
        seller.setBusinessName(sellerReceiveDto.getBusinessName());
        seller.setIsApproved(false);
        Set<Role> s=new HashSet<>();
        s.add(Role.ROLE_USER);
        s.add(Role.ROLE_SELLER);
        user.setRole(s);
         seller.setUser(user);
        seller.setCategories(sellerReceiveDto.getCategories());
        seller.setUser(user);
        seller.setDescription(seller.getDescription());
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
        Users user=userRepo.findByemail(usernameFromContext.fetchUsername()).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));
        user.setContact(userDto.getContact());
        user.setName(userDto.getName());
        if (userDto.getPassword() != null) {
            user.setPassword(bcp.encode(userDto.getPassword()));
        }
    }
    @Transactional
    public void updateAddress(AddressDto addressDto)
    {
        Users user=userRepo.findByemail(usernameFromContext.fetchUsername()).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));
        List<Address>addressList=user.getAddresses();
         Address address=mapToAddress(user,addressDto);
         addressList.add(address);
         user.setAddresses(addressList);

    }
    @Transactional
    public void updateProfilePicture(MultipartFile file) throws IOException {
        String username= usernameFromContext.fetchUsername();
        Users user=userRepo.findByemail(username).orElseThrow(()->new UserNotFoundException("NO USER FOUND"));
        String imageUrl=cloudinaryService.uploadImage(file);
        if(user.getProfileImageUrl()!=null) {
            cloudinaryService.deleteImage(user.getProfileImageUrl());
        }
        user.setProfileImageUrl(imageUrl);
    }

    public UserProfileDto viewProfile() {
       String username=usernameFromContext.fetchUsername();
       Users user=userRepo.findByemail(username).orElseThrow(()->new UserNotFoundException("NO USER"));
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
}
