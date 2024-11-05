package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.BranchStatusService;
import org.edu.restaurantapi.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/branch-status")
public class BranchStatusController {

    @Autowired
    private BranchStatusService service;

    @GetMapping
    public ResponseEntity<?> gets(@RequestParam(value = "name", required = false) String name, Pageable pageable) {
        var response = service.gets(name, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BranchStatus request) {
        var bse = service.findByName(request.getName());
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên trạng thái đã tồn tại"));
        }
        var response = service.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody BranchStatus request) {
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

    @PostMapping("/import-file")
    public ResponseEntity<?> importFile(@RequestBody BranchStatus[] request) throws IOException  {
        return ResponseEntity.ok().body(ApiResponse.BAD_REQUEST("Chưa hoàn thành"));
    }

    private boolean isValid(BranchStatus status) {
        return true; // Logic kiểm tra tính hợp lệ
    }
}
