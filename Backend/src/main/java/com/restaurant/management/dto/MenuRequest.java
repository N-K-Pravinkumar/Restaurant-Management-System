package com.restaurant.management.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private boolean available = true;
    private Integer preparationTime;
    private boolean veg;
    private String imageUrl;
}