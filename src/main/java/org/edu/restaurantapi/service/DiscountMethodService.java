package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.DiscountMethod;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.DiscountMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountMethodService {

    @Autowired
    private DiscountMethodRepository discountMethodRepository;

    // Lấy tất cả các discount method
    public Page<DiscountMethod> getAllDiscountMethods(Pageable pageable) {
        return discountMethodRepository.findDiscountMethodByIsDeleteFalse(pageable);
    }

    // Lấy discount method theo ID
    public DiscountMethod getDiscountMethod(long id) {
        return discountMethodRepository.findById(id).orElse(null);
    }

    // Tạo mới discount method
    public DiscountMethod createDiscountMethod(DiscountMethod discountMethod) {
        Optional<DiscountMethod> existingMethod = discountMethodRepository.findByName(discountMethod.getName());
        if (existingMethod.isPresent()) {
            return null;  // Trả về null nếu phương thức đã tồn tại
        }
        return discountMethodRepository.save(discountMethod);
    }

    // Cập nhật discount method theo ID
    public DiscountMethod updateDiscountMethod(Long id, DiscountMethod updatedMethod) {
        return discountMethodRepository.findById(id).map(existingMethod -> {
            existingMethod.setName(updatedMethod.getName() != null ? updatedMethod.getName() : existingMethod.getName());
            return discountMethodRepository.save(existingMethod);
        }).orElse(null);
    }

    // Xóa discount method theo ID
    public Boolean deleteDiscountMethod(long id) {
        return discountMethodRepository.findById(id).map(discountMethod -> {
            discountMethod.setIsDelete(true);
            discountMethodRepository.save(discountMethod);
            return true;
        }).orElse(false);
    }

    // Tìm discount method theo tên
    public Optional<DiscountMethod> findByName(String name) {
        return discountMethodRepository.findByName(name);
    }

    //check
    public Boolean discountMethodExists(DiscountMethod discountMethod) {
        return discountMethodRepository.findByName(discountMethod.getName()).isPresent();
    }

}
