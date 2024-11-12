package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndIsDeleteFalse(String email);

    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    Page<User> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Page<User> findUserByIsDeleteFalse(Pageable pageable);

    Optional<User> findByEmail(String email);
}
