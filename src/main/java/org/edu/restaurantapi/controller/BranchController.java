package org.edu.restaurantapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    // Create a new branch
    @PostMapping
    public ResponseEntity<?> createBranch(@RequestBody Branch branch) {
        if (branchService.branchNameExists(branch)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Branch already exists"));
        } else if (branchService.branchPhoneNumberExists(branch)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Phone number already exists"));
        } else {
            try {
                Branch createdBranch = branchService.createBranch(branch);
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.CREATED(createdBranch));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Creating branch failed: " + e.getMessage()));
            }
        }
    }

    @GetMapping
    public ResponseEntity<?> getBranch(@RequestParam(value = "name", required = false)String name, @RequestParam(value = "phoneNumber", required = false) String phoneNumber, Pageable pageable) {
        Page<Branch> response;
        if (name != null && !name.isEmpty()) {
            response = branchService.getBranchesByName(pageable, name);
        } else if (phoneNumber != null && !phoneNumber.isEmpty()) {
            response = branchService.getBranchesByPhoneNumber(pageable, phoneNumber);
        } else {
            response = branchService.getAllBranches(pageable);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SUCCESS(response));
    }

    // Get all branches
//    @GetMapping
//    public ResponseEntity<?> getAllBranches(Pageable pageable) {
//        Page<Branch> response = branchService.getAllBranches(pageable);
//        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
//    }


    // Get a single branch by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBranchById(@PathVariable Long id) {
        try {
            Branch response = branchService.getBranchById(id);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the branch with id: " + id));
            }
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the branch with id: " + id));
        }
    }

    // Update a branch by ID
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @RequestBody Branch branchDetails) {
        Branch existingBranch = branchService.getBranchById(id); // Lấy chi nhánh hiện tại theo ID
        if (existingBranch == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the branch with id: " + id));
        }

        if (!existingBranch.getName().equals(branchDetails.getName()) &&
                branchService.branchNameExists(branchDetails)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Branch name already exists for another branch."));
        }
        // Chỉ kiểm tra trùng số điện thoại nếu số điện thoại đã thay đổi
        if (!existingBranch.getPhoneNumber().equals(branchDetails.getPhoneNumber()) &&
                branchService.branchPhoneNumberExists(branchDetails)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Phone number already exists for another branch."));
        }

        try {
            Branch updatedBranch = branchService.updateBranch(id, branchDetails);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(updatedBranch));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Update branch failed: " + e.getMessage()));
        }
    }

    // Delete a branch by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long id) {
        try {
            boolean response = branchService.deleteBranch(id); // Use boolean instead of Boolean

            if (response) {
                return ResponseEntity.ok()
                        .body(ApiResponse.DELETE("Branch deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Branch with id " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Error deleting branch: " + e.getMessage()));
        }
    }
}
