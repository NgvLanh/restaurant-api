package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByUserId(Long userId);
}
