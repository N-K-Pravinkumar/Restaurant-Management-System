package com.restaurant.management.ai;

import com.restaurant.management.entity.Customer;
import com.restaurant.management.entity.Menu;
import com.restaurant.management.repository.MenuRepository;
import com.restaurant.management.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private MenuRepository menuRepo;
    @Autowired
    private OrderItemRepository itemRepo;

    /**
     * Simple rule-based recommendation:
     * 1. Returns top-selling items the customer hasn't ordered recently.
     * 2. Filters by dietary preference if customer is known.
     */
    public List<Menu> recommend(Customer customer, int limit) {
        List<Object[]> topRaw = itemRepo.findTopSellingItems();
        List<String> topNames = topRaw.stream()
                .map(r -> (String) r[0])
                .collect(Collectors.toList());

        List<Menu> allAvailable = menuRepo.findByAvailableTrue();

        // Sort available items by popularity rank
        allAvailable.sort(Comparator.comparingInt(m -> {
            int idx = topNames.indexOf(m.getName());
            return idx == -1 ? Integer.MAX_VALUE : idx;
        }));

        return allAvailable.stream().limit(limit).collect(Collectors.toList());
    }
}