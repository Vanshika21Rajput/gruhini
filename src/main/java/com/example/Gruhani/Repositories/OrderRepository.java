package com.example.Gruhani.Repositories;

import com.example.Gruhani.Enums.OrderStatus;
import com.example.Gruhani.models.Order;
import com.example.Gruhani.models.SellerOrderSummary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"user", "seller", "orderItemList", "orderItemList.product"})
    List<Order> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "seller", "orderItemList", "orderItemList.product"})
    List<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

    @EntityGraph(attributePaths = {"user", "seller", "orderItemList", "orderItemList.product"})
    List<Order> findBySellerId(Long sellerId);

    @EntityGraph(attributePaths = {"user", "seller", "orderItemList", "orderItemList.product"})
    List<Order> findBySellerIdAndOrderStatus(Long sellerId, OrderStatus orderStatus);

    @Query("SELECT o.seller.id as sellerId, o.seller.businessName as businessName, " +
            "o.seller.user.name as sellerName, COUNT(o) as totalOrders " +
            "FROM Order o GROUP BY o.seller.id, o.seller.businessName, o.seller.user.name")
    List<SellerOrderSummary> getOrderCountForSeller();
    @Query("SELECT o.seller.id as sellerId, o.seller.businessName as businessName, " +
            "o.seller.user.name as sellerName, COUNT(o) as totalOrders " +
            "FROM Order o WHERE o.orderStatus = :orderStatus " +
            "GROUP BY o.seller.id, o.seller.businessName, o.seller.user.name")
    List<SellerOrderSummary> getOrderCountForSellerByStatus(@Param("orderStatus") OrderStatus orderStatus);
    @Query("SELECT SUM(o.orderValue) FROM Order o WHERE o.seller.id = :sellerId AND o.orderStatus = 'DELIVERED'")
    BigDecimal sumRevenueBySellerId(Long sellerId);

    long countBySeller_IdAndOrderStatus(Long sellerId, OrderStatus orderStatus);
     long countBySeller_id(Long sellerId);
}