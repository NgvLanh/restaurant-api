package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Address;
import org.edu.restaurantapi.model.BranchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchStatusRepository extends JpaRepository<BranchStatus, Long> {
    Optional<BranchStatus> findByName(String name);

    Page<BranchStatus> findBranchStatusByIsDeleteFalse(Pageable pageable);
}
