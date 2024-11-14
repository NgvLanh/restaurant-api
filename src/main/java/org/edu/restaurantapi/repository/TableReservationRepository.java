package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.TableReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface TableReservationRepository extends JpaRepository<TableReservation, Long> {

    Page<TableReservation> findByIsDeleteFalseAndReservationBranchId(Long branchId,Pageable pageable);

    @Query("SELECT COUNT(tr) AS total_reservations, DATE(tr.createDate) AS date " +
            "FROM tablesReservations tr " +
            "WHERE tr.isDelete = false " +
            "AND MONTH(tr.createDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(tr.createDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY DATE(tr.createDate) " +
            "ORDER BY DATE(tr.createDate)")
    Page<Map<String, Object>> findTotalReservationsPerDay(Pageable pageable);

}
