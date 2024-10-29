package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    Page<OrderStatus> findByNameContaining(String name, Pageable pageable);

    OrderStatus findByName(String name);

    OrderStatus findByNameAndIdNot(String name, Long id);
}
