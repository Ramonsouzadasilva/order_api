package com.order.api.repository;

import com.order.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingAndBrandContainingAndCategoryContaining(String name, String brand, String category);
}
