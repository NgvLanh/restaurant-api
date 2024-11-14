package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndIsDeleteFalse(String email);

    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    Page<User> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Page<User> findUserByIsDeleteFalse(Pageable pageable);

    Optional<User> findByEmail(String email);

    @Query("SELECT MONTH(u.createDate) AS month, COUNT(u) AS totalUsers " +
            "FROM users u " +
            "WHERE u.isDelete = false " +
            "AND YEAR(u.createDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY MONTH(u.createDate) " +
            "ORDER BY MONTH(u.createDate) ASC")
    Page<Map<String, Object>> getUserStatsByMonth(Pageable pageable);

    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM users u WHERE u.isDelete = false")
    Long countTotalRegisteredUsers();
}
