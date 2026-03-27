package com.restaurant.management.controller;

import com.restaurant.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/staff")
    public List<?> getStaff() {
        return userRepo.findAll();
    }
}