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

        catch(
                IOException e){
            throw new RuntimeException(e);
        }


    }
}
