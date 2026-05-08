package com.example.Gruhani.Repositories;

import com.example.Gruhani.Enums.ProductStatus;
import com.example.Gruhani.dtos.ProductDto;
import com.example.Gruhani.models.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    List<Product> findAllByStatus(ProductStatus productStatus);

    @Query("""
SELECT new com.example.Gruhani.dtos.ProductDto(
    p.id,
    p.name,
    p.price,
    p.category,
    p.subcategory,
    p.description,
    p.stock,
    p.status,
    p.rating,
    p.discount,
    p.verified,
    p.message,
    p.deliveryTime,
    p.badge,
    p.seller.id,
    p.image
)                                 
FROM Product p 
WHERE p.status = :status
""")
    List<ProductDto> findAllProductsDto(ProductStatus status);//projection to direclty fetch dto instead of full entity and mapping them

     List<Product> findAllByname(String s);

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.name LIKE %:name%")
    boolean existsBynameLike(@Param("name") String name);

    boolean existsByName(String s);

    Product findByName(String s);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.stock=:currStock where p.id=:id")
    void updateStock(@Param("currStock")int currStock,@Param("id")Long id);

    @Modifying //update and delete operations
    @Transactional
    @Query("UPDATE Product p SET p.status=:status,p.message=:reason where p.id in :ids")
    void batchUpdateStatus(@Param("status") ProductStatus status, @Param("ids")List<Long>ids,@Param("reason")String reason);

    List<Product> findBySellerId(Long id);


    List<Product> findBySellerIdAndStatus(Long id,ProductStatus productStatus);
}
