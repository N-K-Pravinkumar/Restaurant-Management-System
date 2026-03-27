package com.restaurant.management.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingResponse {
    private Long orderId;
    private String orderNumber;
    private int tableNumber;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
}