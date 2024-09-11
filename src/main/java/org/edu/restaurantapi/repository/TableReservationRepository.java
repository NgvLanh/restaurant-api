package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.TableReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableReservationRepository extends JpaRepository<TableReservation, Long> {
}
