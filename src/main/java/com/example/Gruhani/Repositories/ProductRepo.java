package com.example.Gruhani.Repositories;

import com.example.Gruhani.dtos.productdto;
import com.example.Gruhani.models.product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<product,String> {
    List<product> findAllBystatus(String approved);

    product findByid(String id);

     List<product> findAllByname(String s);

    @Query("SELECT COUNT(p) > 0 FROM product p WHERE p.name LIKE %:name%")
    boolean existsBynameLike(@Param("name") String name);

    boolean existsByname(String s);

    product findByname(String s);
}
