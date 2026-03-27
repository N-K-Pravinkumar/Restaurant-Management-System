package com.restaurant.management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String phone;
    private String email;
    private int visitCount = 0;
    private String loyaltyTier; // BRONZE, SILVER, GOLD
    private LocalDateTime lastVisit;
    private Double totalSpent = 0.0;
}