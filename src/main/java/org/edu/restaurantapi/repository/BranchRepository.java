package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
