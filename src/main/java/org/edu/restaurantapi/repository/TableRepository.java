package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long> {
    Page<Table> findTableByIsDeleteFalse(Pageable pageable);
}
