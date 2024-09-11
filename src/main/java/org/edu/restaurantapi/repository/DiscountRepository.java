package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Category;
import org.edu.restaurantapi.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
