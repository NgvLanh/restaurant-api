package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Invoice;
import org.edu.restaurantapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
