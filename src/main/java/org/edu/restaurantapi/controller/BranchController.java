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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long id) {
        var response = BranchService.deleteBranch(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

}
