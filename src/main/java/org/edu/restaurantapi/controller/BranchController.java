package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    @Autowired
    private BranchService service;

    @GetMapping
    public ResponseEntity<?> gets(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                  Pageable pageable) {
        var response = service.gets(name, phoneNumber, pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Branch request) {
        if (service.findByName(request.getName())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên chi nhánh đã tồn tại"));
        } else if (service.findByPhoneNumber(request.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số điện thoại chi nhánh đã tồn tại"));
        }
        var response = service.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Branch request) {
        if (service.findByNameAndIdNot(request.getName(), id)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên chi nhánh đã tồn tại"));
        } else if (service.findByPhoneNumberAndIdNot(request.getPhoneNumber(), id)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số điện thoại chi nhánh đã tồn tại"));
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
