package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Feedback;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepo extends JpaRepository<Feedback,Long> {
    List<Feedback> findBySellerId(Long id);

    boolean existsByOrderId(@NotNull Long orderId);
}
