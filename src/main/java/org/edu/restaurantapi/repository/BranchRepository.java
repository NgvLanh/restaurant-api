package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByPhoneNumberAndIsDeleteFalse(String phoneNumber);

    Optional<Branch> findByNameAndIsDeleteFalse(String name);

    Page<Branch> findBranchByIsDeleteFalse(Pageable pageable);
}
