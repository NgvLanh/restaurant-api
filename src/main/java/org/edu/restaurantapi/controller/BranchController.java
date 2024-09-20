package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        Branch createdBranch = branchService.createBranch(branch);
        return ResponseEntity.ok(createdBranch);
    }

    // Get all branches
    @GetMapping
    public ResponseEntity<List<Branch>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    // Get a single branch by ID
    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        Optional<Branch> branch = branchService.getBranchById(id);
        return branch.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a branch by ID
    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branchDetails) {
        try {
            Branch updatedBranch = branchService.updateBranch(id, branchDetails);
            return ResponseEntity.ok(updatedBranch);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a branch by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}
