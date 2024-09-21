package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.DiscountMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountMethodRepository extends JpaRepository<DiscountMethod, Long> {
    Optional<DiscountMethod> findByName(String name);
}
