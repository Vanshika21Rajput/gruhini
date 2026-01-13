package com.example.Gruhani.Repositories;

import com.example.Gruhani.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {
    List<Orders> findByUserEmail(String userEmail);
    List<Orders> findByChefName(String chefName);
}
