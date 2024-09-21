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
        return discountRepository.findAll(pageable);
    }


    public Discount getDiscount(long discountId) {
        return discountRepository.findById(discountId).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Discount createDiscount(Discount discount) {

        Optional<Discount> existingDiscount = discountRepository.findByCode(discount.getCode());
        if (existingDiscount.isPresent()) {
            return null;  // Trả về null nếu mã giảm giá đã tồn tại
        }
        return discountRepository.save(discount);
    }

    // Cập nhật discount theo ID
    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteDiscount(Long discountId) {
        if (discountRepository.existsById(discountId)){
            discountRepository.deleteById(discountId);
            return true;
        }
        return false;
    }

    //check

    public  Boolean discountCodeExists(Discount discount){
        return discountRepository.findByCode(discount.getCode()).isPresent();
    }

}
