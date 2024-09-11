package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
