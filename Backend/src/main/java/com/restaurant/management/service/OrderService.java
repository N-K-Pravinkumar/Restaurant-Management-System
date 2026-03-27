package com.restaurant.management.service;

import com.restaurant.management.dto.*;
import com.restaurant.management.entity.*;
import com.restaurant.management.exception.ResourceNotFoundException;
import com.restaurant.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private MenuRepository menuRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CustomerRepository customerRepo;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.05"); // 5% GST

    public Order createOrder(OrderRequest req) {
        Order order = new Order();
        order.setTableNumber(req.getTableNumber());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setNotes(req.getNotes());
        order.setPaymentMethod(req.getPaymentMethod());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setOrderNumber("ORD-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

        if (req.getCustomerId() != null)
            customerRepo.findById(req.getCustomerId()).ifPresent(order::setCustomer);
        if (req.getWaiterId() != null)
            userRepo.findById(req.getWaiterId()).ifPresent(order::setWaiter);

        // Save to get ID first
        Order saved = orderRepo.save(order);

        // Build items
        List<OrderItem> items = req.getItems().stream().map(i -> {
            Menu menu = menuRepo.findById(i.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + i.getMenuItemId()));
            BigDecimal total = menu.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()));
            return OrderItem.builder().order(saved).menuItem(menu)
                    .quantity(i.getQuantity()).unitPrice(menu.getPrice()).totalPrice(total)
                    .specialInstructions(i.getSpecialInstructions()).build();
        }).collect(Collectors.toList());

        saved.setItems(items);
        BigDecimal subtotal = items.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal disc = BigDecimal.ZERO;
        saved.setSubtotal(subtotal);
        saved.setTax(tax);
        saved.setDiscount(disc);
        saved.setTotal(subtotal.add(tax).subtract(disc));
        return orderRepo.save(saved);
    }

    public Order getById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<Order> getAll() {
        return orderRepo.findAll();
    }

    public List<Order> getByStatus(Order.OrderStatus status) {
        return orderRepo.findByStatus(status);
    }

    public List<Order> getByTable(int table) {
        return orderRepo.findByTableNumber(table);
    }

    public Order updateStatus(Long id, Order.OrderStatus status) {
        Order o = getById(id);
        o.setStatus(status);
        o.setUpdatedAt(LocalDateTime.now());
        return orderRepo.save(o);
    }

    public OrderResponse toResponse(Order o) {
        return OrderResponse.builder()
                .id(o.getId()).orderNumber(o.getOrderNumber()).tableNumber(o.getTableNumber())
                .status(o.getStatus().name())
                .customerName(o.getCustomer() != null ? o.getCustomer().getName() : null)
                .waiterName(o.getWaiter() != null ? o.getWaiter().getFullName() : null)
                .subtotal(o.getSubtotal()).tax(o.getTax()).discount(o.getDiscount()).total(o.getTotal())
                .paymentMethod(o.getPaymentMethod()).notes(o.getNotes()).createdAt(o.getCreatedAt())
                .items(o.getItems() == null ? List.of() : o.getItems().stream().map(i ->
                        OrderResponse.ItemDTO.builder().id(i.getId())
                                .menuItemName(i.getMenuItem().getName()).category(i.getMenuItem().getCategory())
                                .quantity(i.getQuantity()).unitPrice(i.getUnitPrice()).totalPrice(i.getTotalPrice())
                                .specialInstructions(i.getSpecialInstructions()).build()
                ).collect(java.util.stream.Collectors.toList()))
                .build();
    }
}