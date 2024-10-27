package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.model.TableStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableStatusRepository extends JpaRepository<TableStatus, Long> {

    Page<TableStatus> findByNameContaining(String name, Pageable pageable);

    TableStatus findByName(String name);

    TableStatus findByNameAndIdNot(String name, Long id);

}
