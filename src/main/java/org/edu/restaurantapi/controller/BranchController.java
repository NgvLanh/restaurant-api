package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    // Create a new branch
    @PostMapping
    public ResponseEntity<?> createBranch(@RequestBody Branch branch) {
        if (branchService.branchExists(branch)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Branch already exists"));
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


    // Get all branches
    @GetMapping
    public ResponseEntity<?> getAllBranches(Pageable pageable) {
        Page<Branch> reponse = branchService.getAllBranches(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(reponse));
    }

    // Get a single branch by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBranchById(@PathVariable Long id) {
        try {
            Branch respone = branchService.getBranchById(id);
            if(respone == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the branch with id: " + id));
            }
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(respone));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the branch with id: " + id));
        }

    }

    // Update a branch by ID
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @RequestBody Branch branchDetails) {
        // Kiểm tra xem số điện thoại có bị trùng không

            try {
                Branch updatedBranch = branchService.updateBranch(id, branchDetails);
                if (updatedBranch == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.NOT_FOUND("Not found the branch with id: " + id));
                }
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
