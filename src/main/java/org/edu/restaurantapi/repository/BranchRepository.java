package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.BranchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface BranchRepository extends JpaRepository<Branch, Long> {
   Page<Branch> findByNameContainingAndIsDeleteFalse(String name, Pageable pageable);

   Page<Branch> findByIsDeleteFalse(Pageable pageable);

   Branch findByNameAndIsDeleteFalse(String name);

   Branch findByPhoneNumberAndIsDeleteFalse(String name);

   Branch findByNameAndIsDeleteFalseAndIdNot(String name, Long id);

   Branch findByPhoneNumberAndIsDeleteFalseAndIdNot(String name, Long id);
   @Query("SELECT bs.name AS BranchStatus, COUNT(b.id) AS TotalBranches " +
           "FROM branches b JOIN b.branchStatus bs " +
           "WHERE b.isDelete = false " +
           "GROUP BY bs.name " +
           "ORDER BY TotalBranches DESC")
   Page<Map<String, Object>> getBranchStatisticsByStatus(Pageable pageable);
}
