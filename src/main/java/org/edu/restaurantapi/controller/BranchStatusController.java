package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.BranchStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/branch-status")
public class BranchStatusController {

    @Autowired
    private BranchStatusService branchStatusService;

    // Get all BranchStatuses
    @GetMapping
    public ResponseEntity<?> getAllBranchStatuses(Pageable pageable) {
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(branchStatusService.getAllBranchStatuses(pageable)));
    }

    // Get BranchStatus by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBranchStatusById(@PathVariable Long id) {
        try {
            BranchStatus response = branchStatusService.getBranchStatusById(id);

            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the discount method with id: " + id));
            }

            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.SERVER_ERROR("Lỗi khi lấy trạng thái chi nhánh với id: " + id));
        }
    }

    // Create new BranchStatus
    @PostMapping
    public ResponseEntity<?> createBranchStatus(@Valid @RequestBody BranchStatus branchStatus) {
        System.out.println(branchStatusService.findByName(branchStatus.getName()).isPresent());
        if (branchStatusService.findByName(branchStatus.getName()).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Branch status with this name already exists"));
        } else {
            try {
                BranchStatus response = branchStatusService.createBranchStatus(branchStatus);
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.CREATED(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Failed to create branch status: " + e.getMessage()));
            }
        }
    }

    // Update BranchStatus by ID
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBranchStatus(@PathVariable Long id, @RequestBody BranchStatus branchStatusUpdate) {
        if (branchStatusService.branchStatusNameExist(branchStatusUpdate)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Branch status name already exists"));
        } else {
            try {
                BranchStatus response = branchStatusService.updateBranchStatus(id, branchStatusUpdate);
                if (response == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.NOT_FOUND("Not found the branch status with id: " + id));
                }
                return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Update branch status failed: " + e.getMessage()));
            }
        }
    }

    // Delete BranchStatus by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranchStatus(@PathVariable Long id) {
        try {
            boolean response = branchStatusService.deleteBranchStatus(id);
            if (response) {
                return ResponseEntity.ok().body(ApiResponse.DELETE("Branch status deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Branch status with id " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Error deleting branch status: " + e.getMessage()));
        }
    }
}
