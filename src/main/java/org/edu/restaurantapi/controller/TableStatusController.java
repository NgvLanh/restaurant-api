package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableStatusService;
import org.edu.restaurantapi.service.TableStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/table-statuses")
public class TableStatusController {

    @Autowired
    private TableStatusService service;

    @GetMapping
    public ResponseEntity<?> gets(@RequestParam(value = "name", required = false) String name, Pageable pageable) {
        var response = service.gets(name, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TableStatus request) {
        var bse = service.findByName(request.getName());
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên trạng thái đã tồn tại"));
        }
        var response = service.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TableStatus request) {
        var bse = service.findByNameAndIdNot(request.getName(), id);
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên trạng thái đã tồn tại"));
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
