package com.restaurant.management.controller;

import com.restaurant.management.dto.CustomerRequest;
import com.restaurant.management.entity.Customer;
import com.restaurant.management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return customerService.getById(id);
    }

    @GetMapping("/search")
    public List<Customer> search(@RequestParam String name) {
        return customerService.search(name);
    }

    @GetMapping("/search-phone")
    public List<Customer> searchByPhone(@RequestParam String phone) {
        return customerService.searchByPhone(phone);
    }

    @PostMapping
    public Customer createOrUpdate(@RequestBody CustomerRequest req) {
        return customerService.createOrUpdate(req);
    }
}