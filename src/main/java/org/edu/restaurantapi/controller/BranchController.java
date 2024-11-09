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
    private BranchService branchService;

    @GetMapping
    public ResponseEntity<?> getAllBranches(@RequestParam(value = "name", required = false) Optional<String> name,
                                  @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                  Pageable pageable) {
        var response = branchService.getAllBranches(name, phoneNumber, pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> createBranch(@Valid @RequestBody BranchRequest request) {
        if (branchService.findByName(request.getName())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên chi nhánh đã tồn tại"));
        } else if (branchService.findByPhoneNumber(request.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số điện thoại chi nhánh đã tồn tại"));
        }
        var response = branchService.createBranch(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long id, @RequestBody BranchRequest request) {
        if (branchService.findByNameAndIdNot(request.getName(), id)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên chi nhánh đã tồn tại"));
        } else if (branchService.findByPhoneNumberAndIdNot(request.getPhoneNumber(), id)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số điện thoại chi nhánh đã tồn tại"));
        }
        var response = branchService.updateBranch(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = branchService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
