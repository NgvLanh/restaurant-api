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
import java.util.Optional;

@RestController
@RequestMapping("/api/branch-status")
public class BranchStatusController {

    @Autowired
    private BranchStatusService branchStatusService;

    @GetMapping
    public ResponseEntity<?> getAllBranchStatus(@RequestParam(value = "name", required = false) Optional<String> name, Pageable pageable) {
        var response = branchStatusService.getAllBranchStatus(name, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> createBranchStatus(@Valid @RequestBody BranchStatus request) {
        var bse = branchStatusService.findByName(request.getName());
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên trạng thái đã tồn tại"));
        }
        var response = branchStatusService.createBranchStatus(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBranchStatus(@PathVariable Long id, @Valid @RequestBody BranchStatus request) {
        var bse = branchStatusService.findByNameAndIdNot(request.getName(), id);
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên trạng thái đã tồn tại"));
        }
        var response = branchStatusService.updateBranchStatus(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranchStatus(@PathVariable Long id) {
        var response = branchStatusService.deleteBranchStatus(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
