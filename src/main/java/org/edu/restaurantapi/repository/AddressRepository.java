package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findAddressByIsDeleteFalse(Pageable pageable);
}
