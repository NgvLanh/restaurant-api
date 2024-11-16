package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findCartItemByCartId(Long cartId);

    @Transactional
    void deleteCartItemsByCartIdAndStatusTrue(Long cartId);

    Optional<CartItem> findByCartIdAndDishId(Long cart_id, Long dish_id);
}
