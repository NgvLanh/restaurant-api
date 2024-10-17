package org.edu.restaurantapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/dishes")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public ResponseEntity<?> createDish(@Validated @RequestBody Dish dish,
                                        @RequestParam(value = "branch", required = false) Long branchId) {
        if (dishService.dishExists(dish, branchId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Tên món ăn đã tồn tại"));
        } else {
            try {
                Dish response = dishService.createDish(dish);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.CREATED(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Lỗi khi thêm món ăn: " + e.getMessage()));
            }
        }
    }

    @PatchMapping("/{id}")
    private ResponseEntity<?> updateDish(@PathVariable Long id, @RequestBody Dish dish,
                                         @RequestParam(value = "branch", required = false) Long branchId) {
        Dish existingDish = dishService.getDish(id);
        if (existingDish == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the dish with id: " + id));
        }
        if (dishService.dishExists(dish, branchId) && existingDish.getId() != id) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Name already exists"));
        } else {
            try {
                Dish response = dishService.updateDish(id, dish);
                return ResponseEntity.ok()
                        .body(ApiResponse.SUCCESS(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Updated dish failed: " + e.getMessage()));
            }
        }
    }

//    @GetMapping
//    private ResponseEntity<?> getDishes(Pageable pageable) {
//        Page<Dish> response = dishService.getDishes(pageable);
//        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
//    }

    @GetMapping
    public ResponseEntity<?> getDishes(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "branch", required = false) String branch,
                                       Pageable pageable) {
        Page<Dish> response;
        if(name!= null && !name.isEmpty()){
            response = dishService.getDishByName(name,pageable);
        }else{
            response = dishService.getAllDish(Long.valueOf(branch),pageable);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getDish(@PathVariable Long id) {
        try {
            Dish response = dishService.getDish(id);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the dish with id: " + id));
            }
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.SERVER_ERROR("Not found the dish with id: " + id));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteDish(@PathVariable Long id) {
        try {
            Boolean response = dishService.deleteDish(id);
            if (response) {
                return ResponseEntity.ok()
                        .body(ApiResponse.DELETE("Dish deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Dish with id " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Error deleting dish: " + e.getMessage()));
        }
    }
}
