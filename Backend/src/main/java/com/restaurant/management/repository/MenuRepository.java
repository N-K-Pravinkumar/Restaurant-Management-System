package com.restaurant.management.repository;

import com.restaurant.management.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByCategory(String category);

    List<Menu> findByAvailableTrue();

    List<Menu> findByCategoryAndAvailableTrue(String category);
}