package org.edu.restaurantapi.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Category;
import org.edu.restaurantapi.model.Category;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.repository.CategoryRepository;
import org.edu.restaurantapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> getAllCategories(Optional<String> name, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        if (name.isPresent()) {
            return categoryRepository.findByNameContainingAndActiveTrue(name.get(), pageableSorted);
        }
        return categoryRepository.findByActiveTrue(pageableSorted);

    }

    public Category create(Category request) {
        return categoryRepository.save(request);
    }

    public Category update(Long id, Category request) {
        request.setImage(request.getImage().substring(request.getImage().lastIndexOf("/") + 1));
        return categoryRepository.findById(id).map(c -> {
            c.setName(request.getName() != null ? request.getName() : c.getName());
            // Kiểm tra nếu ảnh không null và không phải là chuỗi rỗng
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                c.setImage(request.getImage());
            }
            c.setDescription(request.getDescription() != null ? request.getDescription() : c.getDescription());
            return categoryRepository.save(c);
        }).orElse(null);
    }


    public Category delete(Long id) {
        return categoryRepository.findById(id).map(c -> {
            c.setActive(false);
            return categoryRepository.save(c);
        }).orElse(null);
    }

    public Boolean findByName(String name) {
        return categoryRepository.findByNameAndActiveTrue(name) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return categoryRepository.findByNameAndIdNotAndActiveTrue(name, id) != null;
    }
}
