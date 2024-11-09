package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

   Page<Branch> findByIsDeleteFalseAndNameContainingOrIsDeleteFalseAndPhoneNumberContaining(String name, String phoneNumber, Pageable pageable);

   Branch findByPhoneNumberAndIdNotAndIsDeleteFalse(String phoneNumber, Long id);

   Branch findByNameAndIdNotAndIsDeleteFalse(String name, Long id);

   Branch findByPhoneNumberAndIsDeleteFalse(String phoneNumber);

   Branch findByNameAndIsDeleteFalse(String name);

   Page<Branch> findByIsDeleteFalse(Pageable pageableSorted);
}
