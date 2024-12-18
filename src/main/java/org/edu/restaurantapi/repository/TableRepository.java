package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TableRepository extends JpaRepository<Table, Long> {

    @Query("SELECT MAX(t.number) FROM tables t WHERE t.active = true AND t.branch.id = :branch")
    Integer findMaxNumberByBranchId(Long branch);

    Table findByNumberAndBranchIdAndActiveTrue(Integer number, Long branch);

    Page<Table> findByBranchIdAndActiveTrue(Long l, Pageable pageableSorted);

    Page<Table> findByBranchIdAndZoneIdAndActiveTrue(Long l, Long z, Pageable pageableSorted);
//    @Query(value = "SELECT DISTINCT t.* " +
//            "FROM tables t " +
//            "LEFT JOIN reservations r " +
//            "ON t.id = r.table_id AND r.booking_date = :date " +
//            "WHERE t.is_delete = FALSE " +
//            "AND t.branch_id = :branchId", nativeQuery = true)
//    List<Table> findAllWithReservationsByBranchId(Long branchId, LocalDate date);

    @Query("SELECT t FROM tables t " +
            "LEFT JOIN FETCH t.reservations r " +
            "WHERE t.active = true " +
            "AND t.branch.id = :branchId")
    List<Table> findAllWithReservationsByBranchId(Long branchId);


}
