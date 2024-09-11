package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
}
