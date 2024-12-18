package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndActiveTrue(String email);

    Optional<User> findByEmailOrPhoneNumberAndActiveTrue(String email, String phoneNumber);

    Page<User> findByPhoneNumberContainingAndBranchIdAndActiveTrue(String phoneNumber, Long branchId, Pageable pageable);

    Page<User> findByPhoneNumberAndBranchIdAndActiveTrue(String phoneNumber, Long branchId, Pageable pageable);

    Optional<User> findByPhoneNumberAndActiveTrue(String phoneNumber);

    Page<User> findUsersByActiveTrue(Pageable pageable);


    @Query("SELECT MONTH(u.createAt) AS month, COUNT(u) AS totalUsers " +
            "FROM users u " +
            "WHERE u.active = true " +
            "AND YEAR(u.createAt) = YEAR(CURRENT_DATE) " +
            "GROUP BY MONTH(u.createAt) " +
            "ORDER BY MONTH(u.createAt) ASC")
    Page<Map<String, Object>> getUserStatsByMonth(Pageable pageable);

    @Query("SELECT COUNT(u) FROM users u " +
            "WHERE u.active = true " +
            "AND :roleName MEMBER OF u.roles")
    Long countTotalRegisteredUsers(String roleName);

    Page<User> findByBranchIdAndActiveTrue(Long l, Pageable pageableSorted);

    @Query("SELECT u FROM users u " +
            "WHERE u.active = true " +
            "AND u.branch.id = :branchId " +
            "AND 'EMPLOYEE' MEMBER OF u.roles")
    Page<User> findUsersByBranchAndRole(
            Long branchId,
            Pageable pageable);

    @Procedure(procedureName = "UpdateRole")
    void updateRole(
            @Param("p_userId") Integer userId,
            @Param("p_branchId") Integer branchId,
            @Param("p_role") String role
    );
}

