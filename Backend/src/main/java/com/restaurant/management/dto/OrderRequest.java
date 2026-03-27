package com.restaurant.management.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private int tableNumber;
    private Long customerId;
    private Long waiterId;
    private List<OrderItemRequest> items;
    private String notes;
    private String paymentMethod;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        private Long menuItemId;
        private int quantity;
        private String specialInstructions;
    }
}