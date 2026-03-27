package com.example.Gruhani.Controllers;

import com.example.Gruhani.Repositories.UserRepo;
import com.example.Gruhani.dtos.AddressDto;
import com.example.Gruhani.dtos.UserDto;
import com.example.Gruhani.dtos.UserProfileDto;
import com.example.Gruhani.service.CloudinaryService;
import com.example.Gruhani.service.ProfileService;
import com.example.Gruhani.service.UsernameFromContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ProfileController {
    @Autowired
    UsernameFromContext usernameFromContext;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    ProfileService profileService;


    @PatchMapping("/image-upload")
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) throws IOException {
          profileService.updateProfilePicture(file);
        return ResponseEntity.ok("Profile picture changed successfully");
    }
    @PatchMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserDto userDto)
    {

        profileService.updateProfile(userDto);
        return ResponseEntity.ok("UPDATED PROFILE SUCCESSFULLY");


    }
    @PatchMapping("/add-address")
    public ResponseEntity<?> addAddress(@RequestBody AddressDto addressDto)
    {
        profileService.updateAddress(addressDto);
          return ResponseEntity.ok("ADDED ADDRESS SUCCESSFULLY");
    }
    @GetMapping("/view-profile")
    public  ResponseEntity<?> viewProfile()
    {
         UserProfileDto userProfileDto=profileService.viewProfile();
         return ResponseEntity.ok(userProfileDto);
    }

}

