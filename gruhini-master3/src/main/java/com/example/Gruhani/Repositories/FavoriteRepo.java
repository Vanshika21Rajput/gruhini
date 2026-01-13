package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepo extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserEmail(String userEmail);
    // basic check if already favorited
    boolean existsByUserEmailAndDishId(String userEmail, String dishId);
    void deleteByUserEmailAndDishId(String userEmail, String dishId);
}
