package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findByNameAndIsDeleteFalse(String name);

    Page<Dish> findByNameContainingAndIsDeleteFalse(String name, Pageable pageable);

    Page<Dish> findDishByIsDeleteFalse(Pageable pageable);
}
