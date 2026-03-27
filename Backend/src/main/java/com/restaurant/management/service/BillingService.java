package com.restaurant.management.service;

import com.restaurant.management.dto.BillingResponse;
import com.restaurant.management.entity.*;
import com.restaurant.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BillingService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private CustomerService customerService;

    public BillingResponse generateBill(Long orderId, String paymentMethod) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(Order.OrderStatus.BILLED);
        order.setPaymentMethod(paymentMethod);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);

        Payment payment = Payment.builder().order(order).amount(order.getTotal())
                .method(paymentMethod).status("SUCCESS")
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .paidAt(LocalDateTime.now()).build();
        paymentRepo.save(payment);

        if (order.getCustomer() != null)
            customerService.recordVisit(order.getCustomer().getId(), order.getTotal().doubleValue());

        return BillingResponse.builder().orderId(order.getId()).orderNumber(order.getOrderNumber())
                .tableNumber(order.getTableNumber()).subtotal(order.getSubtotal())
                .taxAmount(order.getTax()).discountAmount(order.getDiscount())
                .grandTotal(order.getTotal()).paymentMethod(paymentMethod)
                .paymentStatus("SUCCESS").transactionId(payment.getTransactionId()).build();
    }
}