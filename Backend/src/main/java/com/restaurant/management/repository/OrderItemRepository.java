package com.restaurant.management.repository;

import com.restaurant.management.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi.menuItem.name, SUM(oi.quantity) as qty FROM OrderItem oi GROUP BY oi.menuItem.name ORDER BY qty DESC")
    List<Object[]> findTopSellingItems();
}