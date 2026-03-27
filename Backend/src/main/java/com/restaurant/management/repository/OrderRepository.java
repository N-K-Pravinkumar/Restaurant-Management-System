package com.restaurant.management.repository;

import com.restaurant.management.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Order.OrderStatus status);

    List<Order> findByTableNumber(int tableNumber);

    List<Order> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status NOT IN ('BILLED', 'CANCELLED')")
    long countActiveOrders();

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = 'BILLED' AND o.createdAt BETWEEN :from AND :to")
    Double sumRevenueByDateRange(LocalDateTime from, LocalDateTime to);
}