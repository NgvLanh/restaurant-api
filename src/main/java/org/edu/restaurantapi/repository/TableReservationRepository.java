package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.TableReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TableReservationRepository extends JpaRepository<TableReservation, Long> {

    Page<TableReservation> findByIsDeleteFalseAndReservationBranchId(Long branchId,Pageable pageable);
}
