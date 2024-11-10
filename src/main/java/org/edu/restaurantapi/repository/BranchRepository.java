package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
   Page<Branch> findByNameContainingAndIsDeleteFalse(String name, Pageable pageable);

   Page<Branch> findByIsDeleteFalse(Pageable pageable);

   Branch findByNameAndIsDeleteFalse(String name);

   Branch findByPhoneNumberAndIsDeleteFalse(String name);

   Branch findByNameAndIsDeleteFalseAndIdNot(String name, Long id);

   Branch findByPhoneNumberAndIsDeleteFalseAndIdNot(String name, Long id);
}
