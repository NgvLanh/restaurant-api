package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findCategoriesByNameContaining(String name, Pageable pageable);

    Category findByName(String name);

    Category findByNameAndIdNot(String name, Long id);
}