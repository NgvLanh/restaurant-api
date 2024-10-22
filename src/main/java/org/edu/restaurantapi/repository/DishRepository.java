package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findByNameAndIsDeleteFalse(String name);

    Optional<Dish> findByNameAndBranchIdAndIsDeleteFalse(String name, Long brandId);

    Page<Dish> findByNameContainingAndIsDeleteFalse(String name, Pageable pageable);

    Page<Dish> findDishByIsDeleteFalse(Pageable pageable);

    Page<Dish> findDishByBranchIdAndIsDeleteFalse(Long brandId, Pageable pageable);

    Optional<Dish> findByNameAndIdNot(String name, Long id);
}
