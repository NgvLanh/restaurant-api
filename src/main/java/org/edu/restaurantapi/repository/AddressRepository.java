package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
