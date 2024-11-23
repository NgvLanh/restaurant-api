package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {

    Page<Dish> findByBranchIdAndNameContainingAndIsDeleteFalse(Long branchId, String name, Pageable pageable);

    Dish findByNameAndIsDeleteFalse(String name);

    Dish findByNameAndIdNotAndIsDeleteFalse(String name, Long id);

    Page<Dish> findByBranchIdAndCategoryId(Long branchId, Long categoryId, Pageable pageable);

    Page<Dish> findByBranchIdAndIsDeleteFalse(Long branchId, Pageable pageableSorted);

    @Query("SELECT COUNT(d) FROM dishes d WHERE d.isDelete = false")
    long countTotalDishes();
}
