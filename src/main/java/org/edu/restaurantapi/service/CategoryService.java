package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Category;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
    public Category updateCategory(Long id,Category updatedCategory) {
        return categoryRepository.findById(id).map(existingCategory-> {
            existingCategory.setName(updatedCategory.getName()
                    != null ? updatedCategory.getName() : existingCategory.getName());
            existingCategory.setDescription(updatedCategory.getDescription()
                    != null ? updatedCategory.getDescription() : existingCategory.getDescription());
            return categoryRepository.save(existingCategory);
        }).orElse(null);
    }
    public Page<Category> geCategories(Pageable pageable) {
        return categoryRepository.findCategoryByIsDeleteFalse(pageable);
    }
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
    public Boolean deleteCategory(Long id) {
        return categoryRepository.findById(id).map(category -> {
            category.setIsDelete(true);
            categoryRepository.save(category);
            return true;
        }).orElse(false);
    }
    // check name
    public Boolean userCategoryExists(Category category) {
        return categoryRepository.findByName(category.getName()).isPresent();
    }
}
