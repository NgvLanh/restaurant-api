package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}
