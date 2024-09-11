package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableStatusRepository extends JpaRepository<TableStatus, Long> {
}
