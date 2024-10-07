package org.edu.restaurantapi.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.model.DiscountMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByCodeAndIsDeleteFalse(String id);

    Page<Discount> findByCodeContainingAndIsDeleteIsFalse(String code, Pageable pageable);

    Page<Discount> findDiscountByIsDeleteFalse(Pageable pageable);
}
