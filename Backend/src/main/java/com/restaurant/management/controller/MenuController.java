package com.restaurant.management.controller;

import com.restaurant.management.dto.MenuRequest;
import com.restaurant.management.entity.Menu;
import com.restaurant.management.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping
    public List<Menu> getAll() {
        return menuService.getAll();
    }

    @GetMapping("/available")
    public List<Menu> getAvailable() {
        return menuService.getAvailable();
    }

    @GetMapping("/category/{cat}")
    public List<Menu> getByCategory(@PathVariable String cat) {
        return menuService.getByCategory(cat);
    }

    @GetMapping("/{id}")
    public Menu getById(@PathVariable Long id) {
        return menuService.getById(id);
    }

    @PostMapping
    public Menu create(@RequestBody MenuRequest r) {
        return menuService.create(r);
    }

    @PutMapping("/{id}")
    public Menu update(@PathVariable Long id, @RequestBody MenuRequest r) {
        return menuService.update(id, r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/toggle")
    public Menu toggle(@PathVariable Long id) {
        return menuService.toggleAvailability(id);
    }
}