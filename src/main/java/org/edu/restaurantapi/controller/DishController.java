package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.dto.DishDto;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.DishService;
import org.edu.restaurantapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService service;

    @GetMapping
    public ResponseEntity<?> getAllDishes(@RequestParam(value = "name", required = false) Optional<String> name,
                                  Pageable pageable) {
        var response = service.getAllDishes(name, pageable);
        var updateResponse = response.map((res) -> {
            res.setImage("http://localhost:8080/api/files/" + res.getImage());
            return res;
        });
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(updateResponse));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Dish request) throws URISyntaxException, IOException {
        if (service.findByName(request.getName())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên món ăn đã tồn tại"));
        }
        var response = service.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Dish request) throws IOException {
        if (service.findByNameAndIdNot(request.getName(), id)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên món ăn đã tồn tại"));
        }
        var response = service.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getDishesByCategoryId(@PathVariable Long categoryId, Pageable pageable) {
        var response = service.getDishesByCategoryId(categoryId, pageable);
        var updateResponse = response.map((res) -> {
            res.setImage("http://localhost:8080/api/files/" + res.getImage());
            return res;
        });
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(updateResponse));
    }
}
