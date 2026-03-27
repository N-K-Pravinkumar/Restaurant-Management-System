package com.restaurant.management.service;

import com.restaurant.management.dto.*;
import com.restaurant.management.entity.User;
import com.restaurant.management.repository.UserRepository;
import com.restaurant.management.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder encoder;

    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid password");
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return LoginResponse.builder()
                .token(token).username(user.getUsername())
                .role(user.getRole()).fullName(user.getFullName()).userId(user.getId())
                .build();
    }

    public User register(String username, String password, String role, String fullName, String email) {
        if (userRepo.findByUsername(username).isPresent())
            throw new RuntimeException("Username already exists");
        User user = User.builder()
                .username(username).password(encoder.encode(password))
                .role(role).fullName(fullName).email(email).active(true).build();
        return userRepo.save(user);
    }
}