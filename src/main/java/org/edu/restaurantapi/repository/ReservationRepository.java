package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByBranchIdAndIsConflictTrue(Long branch, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM reservations r " +
            "WHERE r.table.id = :tableId " +
            "AND r.bookingDate = :bookingDate " +
            "AND CAST(:time AS localtime) BETWEEN r.startTime AND r.endTime")
    Boolean isTableReserved(@Param("tableId") Long tableId,
                            @Param("bookingDate") LocalDate bookingDate,
                            @Param("time") LocalTime time);
}
