package com.restaurant.management.controller;

import com.restaurant.management.dto.*;
import com.restaurant.management.entity.Order;
import com.restaurant.management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BillingService billingService;

    @GetMapping
    public List<OrderResponse> getAll() {
        return orderService.getAll().stream().map(orderService::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable Long id) {
        return orderService.toResponse(orderService.getById(id));
    }

    @GetMapping("/status/{status}")
    public List<OrderResponse> getByStatus(@PathVariable String status) {
        return orderService.getByStatus(Order.OrderStatus.valueOf(status.toUpperCase()))
                .stream().map(orderService::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/table/{table}")
    public List<OrderResponse> getByTable(@PathVariable int table) {
        return orderService.getByTable(table).stream().map(orderService::toResponse).collect(Collectors.toList());
    }

    @PostMapping
    public OrderResponse create(@RequestBody OrderRequest req) {
        return orderService.toResponse(orderService.createOrder(req));
    }

    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return orderService.toResponse(orderService.updateStatus(id, Order.OrderStatus.valueOf(body.get("status").toUpperCase())));
    }

    @PostMapping("/{id}/bill")
    public BillingResponse bill(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return billingService.generateBill(id, body.getOrDefault("paymentMethod", "CASH"));
    }
}