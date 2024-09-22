package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByPhoneNumber(String phoneNumber);
}
