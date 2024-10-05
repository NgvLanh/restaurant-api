package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndIsDeleteFalse(String name);
    Page<Category> findByNameContainingAndIsDeleteFalse(String name,Pageable pageable);

    Page<Category> findCategoryByIsDeleteFalse(Pageable pageable);
}