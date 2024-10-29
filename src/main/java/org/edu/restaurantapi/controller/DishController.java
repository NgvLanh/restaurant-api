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


@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService service;

    @GetMapping
    public ResponseEntity<?> gets(@RequestParam(value = "name", required = false) String name,
                                  Pageable pageable) {
        var response = service.gets(name, pageable);
        var updateResponse = response.map((res) -> {
            res.setImage("http://localhost:8080/api/files/" + res.getImage());
            return res;
        });
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(updateResponse));
    }

    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute DishDto request) throws URISyntaxException, IOException {
        String fileName = FileService.saveFile(request.getFile());
        var dish = Dish.builder()
                .name(request.getName())
                .image(fileName)
                .price(request.getPrice())
                .description(request.getDescription())
                .category(request.getCategory())
                .status(true)
                .isDelete(false)
                .build();
        if (service.findByName(request.getName())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên món ăn đã tồn tại"));
        }
        var response = service.create(dish);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @ModelAttribute DishDto request) throws IOException {
        if (service.findByNameAndIdNot(request.getName(), id)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên món ăn đã tồn tại"));
        }
        String fileName = null;
        if (request.getFile() != null && !request.getFile().isEmpty()) {
            fileName = FileService.saveFile(request.getFile());
        } else {
            fileName = request.getImageUrl();
        }
        var dish = Dish.builder()
                .id(id)
                .name(request.getName())
                .image(fileName)
                .price(request.getPrice())
                .description(request.getDescription())
                .category(request.getCategory())
                .status(true)
                .isDelete(false)
                .build();
        var response = service.update(id, dish);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
