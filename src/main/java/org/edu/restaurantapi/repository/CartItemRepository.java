package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findCartItemByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndDishId(Long cart_id, Long dish_id);
}
