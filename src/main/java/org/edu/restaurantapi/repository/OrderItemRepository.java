package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findOrderItemsByOrderId(Long orderId);
}
