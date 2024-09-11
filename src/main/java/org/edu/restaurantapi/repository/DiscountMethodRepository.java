package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.model.DiscountMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountMethodRepository extends JpaRepository<DiscountMethod, Long> {
}
