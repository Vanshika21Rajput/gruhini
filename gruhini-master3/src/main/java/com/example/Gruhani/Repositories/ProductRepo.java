package com.example.Gruhani.Repositories;

import com.example.Gruhani.dtos.productdto;
import com.example.Gruhani.models.product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<product,String> {
    List<product> findAllBystatus(String approved);

    Optional<product> findByid(Long id);

     List<product> findAllByname(String s);

    @Query("SELECT COUNT(p) > 0 FROM product p WHERE p.name LIKE %:name%")
    boolean existsBynameLike(@Param("name") String name);

    boolean existsByname(String s);

    product findByname(String s);

    // Find all products by seller
    java.util.List<product> findAllBySeller(com.example.Gruhani.models.Seller seller);
}
