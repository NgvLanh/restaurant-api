package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.model.DiscountMethod;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.DiscountMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/discount-methods")
public class DiscountMethodController {

    @Autowired
    private DiscountMethodService discountMethodService;

    // Lấy tất cả discount methods
    @GetMapping
    public ResponseEntity<?> getAllDiscountMethods(Pageable pageable) {
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(discountMethodService.getAllDiscountMethods(pageable)));
    }

    // Lấy discount method theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscountMethodById(@PathVariable Long id ) {
        try {
            DiscountMethod response = discountMethodService.getDiscountMethod(id);
            if (response==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the discount method with id: " + id));
            }
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.SERVER_ERROR("Discount method ERROR id: " + id));
        }
    }

    // Tạo discount method mới
    @PostMapping
    public ResponseEntity<?> createDiscountMethod(@Valid @RequestBody DiscountMethod discountMethod) {
        if (discountMethodService.findByName(discountMethod.getName()).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Discount method with this name already exists"));
        } else {
            try {
                DiscountMethod response = discountMethodService.createDiscountMethod(discountMethod);
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.CREATED(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Failed to create discount method: " + e.getMessage()));
            }
        }
    }

    // Cập nhật discount method theo ID
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateDiscountMethod(@PathVariable Long id, @RequestBody DiscountMethod discountMethod) {
        if (discountMethodService.discountMethodExists(discountMethod)){
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Name already exists"));
        }else {
            try {

                DiscountMethod response = discountMethodService.updateDiscountMethod(id,discountMethod);
                if (response==null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.NOT_FOUND("Not found the discount method with id: " + id));
                }
                return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
            }catch (Exception e){
                return  ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Update discount method failed" + e.getMessage()));

            }
        }

    }

    // Xóa discount method theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscountMethod(@PathVariable Long id) {
        try {
            Boolean response = discountMethodService.deleteDiscountMethod(id);
            if (response) {
                return ResponseEntity.ok().body(ApiResponse.DELETE("Discount method deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.NOT_FOUND("Discount method with id " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.SERVER_ERROR("Error deleting discount method: " + e.getMessage()));
        }
    }

    // Tìm discount method theo tên
    @GetMapping("/name/{name}")
    public ResponseEntity<?> findDiscountMethodByName(@PathVariable String name) {
        Optional<DiscountMethod> discountMethod = discountMethodService.findByName(name);
        if (discountMethod.isPresent()) {
            return ResponseEntity.ok(ApiResponse.SUCCESS(discountMethod.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.NOT_FOUND("Discount method with name " + name));
        }
    }
}
