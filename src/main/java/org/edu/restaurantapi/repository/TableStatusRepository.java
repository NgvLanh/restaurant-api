package org.edu.restaurantapi.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableStatusRepository extends JpaRepository<TableStatus, Long> {
    Page<TableStatus> findTableStatusByIsDeleteFalse(Pageable pageable);

    Optional<TableStatus> findTableStatusByNameAndIsDeleteFalse(String tableStatusName);

    Page<TableStatus> findByNameContainingAndIsDeleteFalse(String name, Pageable pageable);

}
