package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
