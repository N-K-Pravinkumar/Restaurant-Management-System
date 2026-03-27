package com.restaurant.management.service;

import com.restaurant.management.dto.CustomerRequest;
import com.restaurant.management.entity.Customer;
import com.restaurant.management.exception.ResourceNotFoundException;
import com.restaurant.management.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public List<Customer> getAll() {
        return customerRepo.findAll();
    }

    public Customer getById(Long id) {
        return customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public List<Customer> search(String name) {
        return customerRepo.findByNameContainingIgnoreCase(name);
    }

    public List<Customer> searchByPhone(String phone) {
        return customerRepo.findByPhoneContaining(phone);
    }

    public Customer createOrUpdate(CustomerRequest req) {
        return customerRepo.findByPhone(req.getPhone()).map(c -> {
            c.setName(req.getName());
            c.setEmail(req.getEmail());
            return customerRepo.save(c);
        }).orElseGet(() -> {
            Customer c = Customer.builder().name(req.getName()).phone(req.getPhone())
                    .email(req.getEmail()).visitCount(0).totalSpent(0.0).loyaltyTier("BRONZE").build();
            return customerRepo.save(c);
        });
    }

    public void recordVisit(Long customerId, double amount) {
        Customer c = getById(customerId);
        c.setVisitCount(c.getVisitCount() + 1);
        c.setTotalSpent(c.getTotalSpent() + amount);
        c.setLastVisit(LocalDateTime.now());
        // Update loyalty tier
        double spent = c.getTotalSpent();
        if (spent >= 10000) c.setLoyaltyTier("GOLD");
        else if (spent >= 3000) c.setLoyaltyTier("SILVER");
        else c.setLoyaltyTier("BRONZE");
        customerRepo.save(c);
    }
}