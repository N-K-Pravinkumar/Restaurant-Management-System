package com.restaurant.management.controller;
import com.restaurant.management.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins="http://localhost:4200")
public class AnalyticsController {
    @Autowired private AnalyticsService analyticsService;

    @GetMapping("/dashboard") public Map<String,Object> dashboard() { return analyticsService.getDashboard(); }
}