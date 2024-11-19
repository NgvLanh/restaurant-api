package org.edu.restaurantapi.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.dto.DishDto;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.DishRepository;
import org.edu.restaurantapi.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DishService {

    @Autowired
    private DishRepository repository;
    @Autowired
    private DishRepository dishRepository;

    public Page<Dish> getAllDishes(Optional<Long> branch, Optional<String> name, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        if (name.isPresent()) {
            return repository.findByBranchIdAndNameContainingAndIsDeleteFalse(branch.get(), name.get(), pageableSorted);
        }
        return repository.findByIsDeleteFalse(pageableSorted);
    }

    public Dish create(Dish request) {
        return repository.save(request);
    }

    public Dish update(Long id, Dish request) {
        request.setImage(request.getImage().substring(request.getImage().lastIndexOf("/") + 1));
        return repository.findById(id).map(d -> {
            d.setName(request.getName() != null ? request.getName() : d.getName());
            d.setPrice(request.getPrice() != null ? request.getPrice() : d.getPrice());
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                d.setImage(request.getImage());
            }
            d.setDescription(request.getDescription() != null ? request.getDescription() : d.getDescription());
            d.setCategory(request.getCategory() != null ? request.getCategory() : d.getCategory());
            return repository.save(d);
        }).orElse(null);
    }

    public Boolean delete(Long id) {
        return repository.findById(id).map(b -> {
            b.setIsDelete(true);
            repository.save(b);
            return true;
        }).orElse(false);
    }

    public Boolean findByName(String name) {
        return repository.findByNameAndIsDeleteFalse(name) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return repository.findByNameAndIdNotAndIsDeleteFalse(name, id) != null;
    }

    public Page<Dish> getDishesByCategoryId(Long branchId, Long categoryId, Pageable pageable) {
        return repository.findByBranchIdAndCategoryId(branchId, categoryId, pageable);
    }

    public Long countTotalDishes() {
        return dishRepository.countTotalDishes();
    }
}
