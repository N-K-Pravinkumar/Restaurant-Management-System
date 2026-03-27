package com.restaurant.management.service;

import com.restaurant.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalyticsService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private OrderItemRepository itemRepo;
    @Autowired
    private CustomerRepository customerRepo;

    public Map<String, Object> getDashboard() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay();

        Double todayRevenue = orderRepo.sumRevenueByDateRange(today, todayEnd);
        Double monthRevenue = orderRepo.sumRevenueByDateRange(monthStart, todayEnd);
        List<Object[]> topItems = itemRepo.findTopSellingItems();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("todayRevenue", todayRevenue != null ? todayRevenue : 0.0);
        data.put("monthRevenue", monthRevenue != null ? monthRevenue : 0.0);
        data.put("totalOrders", orderRepo.count());
        data.put("activeOrders", orderRepo.countActiveOrders());
        data.put("totalCustomers", customerRepo.count());
        data.put("totalMenuItems", 0);
        data.put("pendingOrders", orderRepo.findByStatus(com.restaurant.management.entity.Order.OrderStatus.PENDING).size());
        data.put("preparingOrders", orderRepo.findByStatus(com.restaurant.management.entity.Order.OrderStatus.PREPARING).size());
        data.put("readyOrders", orderRepo.findByStatus(com.restaurant.management.entity.Order.OrderStatus.READY).size());

        List<Map<String, Object>> top = new ArrayList<>();
        for (int i = 0; i < Math.min(5, topItems.size()); i++) {
            Object[] row = topItems.get(i);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", row[0]);
            item.put("quantity", row[1]);
            top.add(item);
        }
        data.put("topSellingItems", top);
        return data;
    }
}