package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
