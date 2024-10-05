package org.edu.restaurantapi.controller;


import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Category;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(@Validated @RequestBody Category category) {
        if (categoryService.categoryExists(category)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Name already exists"));
        } else {
            try {
                Category response = categoryService.createCategory(category);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.CREATED(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Created category failed: " + e.getMessage()));
            }
        }
    }

    @PatchMapping("/{id}")
    private ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category existingCategory = categoryService.getCategory(id);

        if (existingCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the category with id: " + id));
        }

        if (categoryService.categoryExists(category) &&
                !existingCategory.getName().equals(category.getName())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Name already exists"));
        }

        try {
            Category response = categoryService.updateCategory(id, category);
            return ResponseEntity.ok()
                    .body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Updated category failed: " + e.getMessage()));
        }
    }

    //    @GetMapping
//    private ResponseEntity<?> getCategory(Pageable pageable) {
//        Page<Category> response = categoryService.geCategories(pageable);
//        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
//    }
    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(value = "name", required = false) String name, Pageable pageable) {
        Page<Category> response;
        if (name != null && !name.isEmpty()) {
            response = categoryService.getCategoriesByName(name, pageable);
        } else {
            response = categoryService.getAllCategory(pageable);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getCategory(@PathVariable Long id) {
        try {
            Category response = categoryService.getCategory(id);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the category with id: " + id));
            }
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the category with id: " + id));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            Boolean response = categoryService.deleteCategory(id);
            if (response) {
                return ResponseEntity.ok()
                        .body(ApiResponse.DELETE("Category deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Category with id " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Error deleting category: " + e.getMessage()));
        }
    }
}
