package com.example.Gruhani.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryService {
    @Autowired
    Cloudinary cloudinary;
    public String uploadImage(MultipartFile file) throws IOException {

        Map cloudresult;
        try {
            cloudresult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            return cloudresult.get("secure_url").toString();

        }

        catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    public void deleteImage(String imageUrl) {
        try {
            // Extract public_id from URL
            // Cloudinary URL format: https://res.cloudinary.com/{cloud}/image/upload/v123/{publicId}.jpg
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }

    private String extractPublicId(String imageUrl) {
        // Extract everything after "upload/" and remove extension
        String[] parts = imageUrl.split("upload/");
        String withVersion = parts[1]; // v123456/publicId.jpg
        String withoutVersion = withVersion.replaceFirst("v\\d+/", ""); // publicId.jpg
        return withoutVersion.substring(0, withoutVersion.lastIndexOf(".")); // publicId
    }
}
