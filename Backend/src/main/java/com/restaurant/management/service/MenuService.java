package com.restaurant.management.service;

import com.restaurant.management.dto.MenuRequest;
import com.restaurant.management.entity.Menu;
import com.restaurant.management.exception.ResourceNotFoundException;
import com.restaurant.management.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepo;

    public List<Menu> getAll() {
        return menuRepo.findAll();
    }

    public List<Menu> getAvailable() {
        return menuRepo.findByAvailableTrue();
    }

    public List<Menu> getByCategory(String cat) {
        return menuRepo.findByCategory(cat);
    }

    public Menu getById(Long id) {
        return menuRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + id));
    }

    public Menu create(MenuRequest req) {
        Menu m = Menu.builder().name(req.getName()).description(req.getDescription())
                .price(req.getPrice()).category(req.getCategory()).available(req.isAvailable())
                .preparationTime(req.getPreparationTime()).veg(req.isVeg()).imageUrl(req.getImageUrl()).build();
        return menuRepo.save(m);
    }

    public Menu update(Long id, MenuRequest req) {
        Menu m = getById(id);
        if (req.getName() != null) m.setName(req.getName());
        if (req.getDescription() != null) m.setDescription(req.getDescription());
        if (req.getPrice() != null) m.setPrice(req.getPrice());
        if (req.getCategory() != null) m.setCategory(req.getCategory());
        if (req.getPreparationTime() != null) m.setPreparationTime(req.getPreparationTime());
        m.setAvailable(req.isAvailable());
        m.setVeg(req.isVeg());
        return menuRepo.save(m);
    }

    public void delete(Long id) {
        menuRepo.deleteById(id);
    }

    public Menu toggleAvailability(Long id) {
        Menu m = getById(id);
        m.setAvailable(!m.isAvailable());
        return menuRepo.save(m);
    }
}