package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

     Cart findByu_id(Long uId);
}
