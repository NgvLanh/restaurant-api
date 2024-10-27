package org.edu.restaurantapi.controller;


import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Category;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<?> gets(@RequestParam(value = "name", required = false) String name, Pageable pageable) {
        var response = service.gets(name, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Category request) {
        var cae = service.findByName(request.getName());
        if (cae) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên loại món ăn đã tồn tại"));
        }
        var response = service.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Category request) {
        var cae = service.findByNameAndIdNot(request.getName(), id);
        if (cae) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên loại món ăn đã tồn tại"));
        }
        var response = service.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
