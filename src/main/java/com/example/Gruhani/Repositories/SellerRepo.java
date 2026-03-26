package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Seller;
import com.example.Gruhani.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Long> {
    Seller findByuser_email(String username);



    List<Seller> findByIsApproved(boolean b);

    boolean existsByUser(Users user);

    Optional<Seller> findByUser(Users user);


    //Seller findByemail(String username);
}
