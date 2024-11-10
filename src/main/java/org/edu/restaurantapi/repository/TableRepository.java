package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TableRepository extends JpaRepository<Table, Long> {

    @Query("SELECT t" +
            " FROM tables t" +
            " WHERE t.isDelete = false" +
            " AND t.branch.id = :branch" +
            " AND (:number = 0 OR t.number = :number)")
    Page<Table> findByIsDeleteFalseAndBranchIdAndNumber(Long branch, Integer number, Pageable pageable);

    @Query("SELECT MAX(t.number) FROM tables t WHERE t.isDelete = false AND t.branch.id = :branch")
    Integer findMaxNumberByBranchId(Long branch);

    Table findByIsDeleteFalseAndNumberAndBranchIs(Integer number, Branch branch);

    Page<Table> findByIsDeleteFalseAndBranchId(Long l, Pageable pageableSorted);
}
