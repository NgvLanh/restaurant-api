package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findCartItemByCartId(Long cartId);
}
