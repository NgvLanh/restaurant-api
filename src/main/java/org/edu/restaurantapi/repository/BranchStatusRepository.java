package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Address;
import org.edu.restaurantapi.model.BranchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchStatusRepository extends JpaRepository<BranchStatus, Long> {
}
