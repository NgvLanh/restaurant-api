package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.DiscountMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountMethodRepository extends JpaRepository<DiscountMethod, Long> {

    Page<DiscountMethod> findDiscountMethodByNameContaining(String name, Pageable pageable);

    DiscountMethod findByName(String name);

    DiscountMethod findByNameAndIdNot(String name, Long id);
}
