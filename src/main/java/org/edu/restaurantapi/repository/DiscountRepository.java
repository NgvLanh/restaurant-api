package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Page<Discount> findByCodeContainingAndIsDeleteFalse(String code, Pageable pageable);

    Discount findByCodeAndIsDeleteFalse(String code);

    Discount findByCodeAndIdNotAndIsDeleteFalse(String code, Long id);
}
