package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.request.BranchRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/branches")
public class BranchController {
    @Autowired
    private BranchService BranchService;

    @GetMapping
    public ResponseEntity<?> getAllBranch(@RequestParam(value = "name", required = false) Optional<String> name, Pageable pageable) {
        var response = BranchService.getAllBranches(name, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBranchByUserId(@PathVariable Long id) {
        var response = BranchService.findByUserId(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> createBranch(@Valid @RequestBody BranchRequest request) {
        var branchNameExists = BranchService.findByName(request.getName());
        var branchPhoneNumberExists = BranchService.findByPhoneNumber(request.getPhoneNumber());
        if (branchNameExists) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên chi nhánh đã tồn tại"));
        } else if (branchPhoneNumberExists) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số điện thoại chi nhánh đã tồn tại"));
        }
        var response = BranchService.createBranch(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @Valid @RequestBody BranchRequest request) {
        var branchNameExists = BranchService.findByNameAndIdNot(request.getName(), id);
        var branchPhoneNumberExists = BranchService.findByPhoneNumberAndIdNot(request.getPhoneNumber(), id);
        if (branchNameExists) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên chi nhánh đã tồn tại"));
        } else if (branchPhoneNumberExists) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số điện thoại chi nhánh đã tồn tại"));
        }
        var response = BranchService.updateBranch(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/auth/{id}")
    public ResponseEntity<?> updateBranch01(@PathVariable Long id, @Valid @RequestBody Branch request) {
        var branchNameExists = BranchService.findByNameAndIdNot(request.getName(), id);
        var branchPhoneNumberExists = BranchService.findByPhoneNumberAndIdNot(request.getPhoneNumber(), id);
        if (branchNameExists) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên chi nhánh đã tồn tại"));
        } else if (branchPhoneNumberExists) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số điện thoại chi nhánh đã tồn tại"));
        }
        var response = BranchService.updateBranch01(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long id) {
        var response = BranchService.deleteBranch(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getBranchStatisticsByStatus(Pageable pageable) {
        var response = BranchService.getBranchStatisticsByStatus(pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

}
