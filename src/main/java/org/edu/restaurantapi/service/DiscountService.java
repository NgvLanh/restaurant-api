package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public Page<Discount> getAllDiscounts(Pageable pageable) {
        return discountRepository.findDiscountByIsDeleteFalse(pageable);
    }


    public Discount getDiscount(long discountId) {
        return discountRepository.findById(discountId).orElse(null);
    }

    public Discount createDiscount(Discount discount) {

        Optional<Discount> existingDiscount = discountRepository.findByCodeAndIsDeleteFalse(discount.getCode());
        if (existingDiscount.isPresent()) {
            return null;
        }
        return discountRepository.save(discount);
    }

    // Cập nhật discount theo ID
    public Discount updateDiscount(Long discountId, Discount updatedDiscount) {

        return discountRepository.findById(discountId).map(existingDiscount -> {
            existingDiscount.setCode(updatedDiscount.getCode() != null ? updatedDiscount.getCode() : existingDiscount.getCode());
            existingDiscount.setQuantity(updatedDiscount.getQuantity() != null ? updatedDiscount.getQuantity() : existingDiscount.getQuantity());
            existingDiscount.setExpirationDate(updatedDiscount.getExpirationDate() != null ? updatedDiscount.getExpirationDate() : existingDiscount.getExpirationDate());
            existingDiscount.setMethod(updatedDiscount.getMethod() != null ? updatedDiscount.getMethod() : existingDiscount.getMethod());
            existingDiscount.setQuota(updatedDiscount.getQuota() != null ? updatedDiscount.getQuota() : existingDiscount.getQuota());
            existingDiscount.setValue(updatedDiscount.getValue() != null ? updatedDiscount.getValue() : existingDiscount.getValue());
            return discountRepository.save(existingDiscount);
        }).orElse(null);
    }

    public Boolean deleteDiscount(Long id) {
        return discountRepository.findById(id).map(discount -> {
            discount.setIsDelete(true);
            discountRepository.save(discount);
            return true;
        }).orElse(false);
    }

    //check
    public  Boolean discountCodeExists(Discount discount){
        return discountRepository.findByCodeAndIsDeleteFalse(discount.getCode()).isPresent();
    }

}
