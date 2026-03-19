package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Long> {
    Seller findByuser_email(String username);


    //Seller findByemail(String username);
}
