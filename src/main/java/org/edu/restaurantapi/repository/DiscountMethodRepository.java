package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.DiscountMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountMethodRepository extends JpaRepository<DiscountMethod, Long> {
    Optional<DiscountMethod> findByNameAndIsDeleteFalse(String name);

    Page<DiscountMethod> findByNameContainingAndIsDeleteFalse(String name, Pageable pageable);

    Page<DiscountMethod> findDiscountMethodByIsDeleteFalse(Pageable pageable);
}
