package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    Page<Zone> findByNameContainingAndBranchIdAndActiveTrue(String name, Long branch, Pageable pageable);

    Zone findByNameAndActiveTrue(String name);

    Zone findByNameAndIdNotAndActiveTrue(String name, Long id);
}
