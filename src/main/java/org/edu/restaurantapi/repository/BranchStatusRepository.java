package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.BranchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchStatusRepository extends JpaRepository<BranchStatus, Long> {

    Page<BranchStatus> findByNameContainingAndActiveTrue(String name, Pageable pageable);

    Page<BranchStatus> findBranchStatusByActiveTrue(Pageable pageable);

    BranchStatus findByNameAndActiveTrue(String name);

    BranchStatus findByNameAndIdNotAndActiveTrue(String name, Long id);
}
