package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DishService {
    @Autowired
    private DishRepository dishRepository;

    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish updateDish(Long id, Dish updatedDish) {
        return dishRepository.findById(id).map(existingDish -> {
            existingDish.setName(updatedDish.getName()
                    != null ? updatedDish.getName() : existingDish.getName());
            existingDish.setImage(updatedDish.getImage()
                    != null ? updatedDish.getImage() : existingDish.getImage());
            existingDish.setPrice(updatedDish.getPrice()
                    != null ? updatedDish.getPrice() : existingDish.getPrice());
            existingDish.setCategory(updatedDish.getCategory()
                    != null ? updatedDish.getCategory() : existingDish.getCategory());
            existingDish.setDescription(updatedDish.getDescription()
                    != null ? updatedDish.getDescription() : existingDish.getDescription());
            return dishRepository.save(existingDish);
        }).orElse(null);
    }

    public Page<Dish> getDishes(Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return dishRepository.findDishByIsDeleteFalse(pageableSorted);
    }

    public Dish getDish(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    public Boolean deleteDish(Long id) {
        return dishRepository.findById(id).map(dish -> {
            dish.setIsDelete(true);
            dishRepository.save(dish);
            return true;
        }).orElse(false);
    }

    public Page<Dish> getAllDish(Long branch, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return dishRepository.findDishByBranchIdAndIsDeleteFalse(branch, pageableSorted);
    }

    // check name
    public Boolean dishExists(Dish dish, Long branchId) {
        return dishRepository.findByNameAndBranchIdAndIsDeleteFalse(dish.getName(), branchId).isPresent();
    }

    public Page<Dish> getDishByName(String name, Pageable pageable) {
        return dishRepository.findByNameContainingAndIsDeleteFalse(name, pageable);
    }

}
