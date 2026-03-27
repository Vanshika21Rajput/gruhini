package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {


    Optional<Cart> findByUser_Id(Long userId);

    Optional<Cart> findByUser_Email(String email);

    @Query("""
        SELECT DISTINCT c FROM Cart c 
        JOIN FETCH c.cartItems ci 
        JOIN FETCH ci.product 
        WHERE c.user.email = :email
    """)
    Optional<Cart> findCartWithItems(@Param("email") String email);
}

