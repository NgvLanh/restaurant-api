package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.service.BranchStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/branchstatus")
public class BranchStatusController {

    private final BranchStatusService branchStatusService;

    @Autowired
    public BranchStatusController(BranchStatusService branchStatusService) {
        this.branchStatusService = branchStatusService;
    }

    // Get all BranchStatuses
    @GetMapping
    public ResponseEntity<List<BranchStatus>> getAllBranchStatuses() {
        List<BranchStatus> branchStatuses = branchStatusService.getAllBranchStatuses();
        return ResponseEntity.ok(branchStatuses);
    }

    // Get BranchStatus by ID
    @GetMapping("/{id}")
    public ResponseEntity<BranchStatus> getBranchStatusById(@PathVariable Long id) {
        Optional<BranchStatus> branchStatus = branchStatusService.getBranchStatusById(id);
        return branchStatus.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new BranchStatus
    @PostMapping
    public ResponseEntity<BranchStatus> createBranchStatus(@RequestBody BranchStatus branchStatus) {
        BranchStatus createdBranchStatus = branchStatusService.createBranchStatus(branchStatus);
        return ResponseEntity.ok(createdBranchStatus);
    }

    // Update BranchStatus by ID
    @PutMapping("/{id}")
    public ResponseEntity<BranchStatus> updateBranchStatus(@PathVariable Long id, @RequestBody BranchStatus branchStatusUpdate) {
        Optional<BranchStatus> updatedBranchStatus = branchStatusService.updateBranchStatus(id, branchStatusUpdate);
        return updatedBranchStatus.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete BranchStatus by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchStatus(@PathVariable Long id) {
        branchStatusService.deleteBranchStatus(id);
        return ResponseEntity.noContent().build();
    }
}
