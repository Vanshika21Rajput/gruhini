package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,String> {


    //Optional<CartItem> findByC_idAndP_id(Long id, Long productId);

    Optional<CartItem> findByCartIdAndProductId(Long id, Long productid);
}
