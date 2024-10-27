package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.BranchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchStatusRepository extends JpaRepository<BranchStatus, Long> {

    Page<BranchStatus> findByNameContaining(String name, Pageable pageable);

    BranchStatus findByName(String name);

    BranchStatus findByNameAndIdNot(String name, Long id);
}
