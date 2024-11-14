package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Address;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping
    private ResponseEntity<?> createAddress(@Valid @RequestBody Address address) {
        try {
            Address response = addressService.createAddress(address);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.CREATED(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Created Address failed: " + e.getMessage()));
        }

    }

    @GetMapping
    private ResponseEntity<?> getAddress(Pageable pageable) {
        Page<Address> response = addressService.getAddresses(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getAddress(@PathVariable Long id) {
        Address response = addressService.getAddress(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the Address with id: " + id));
        }
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }


    @PatchMapping("/{id}")
    private ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        try {
            Address response = addressService.updateAddress(id, address);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the Address with id: " + id));
            }
            return ResponseEntity.ok()
                    .body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Updated Address failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            Boolean response = addressService.deleteAddress(id);
            if (response) {
                return ResponseEntity.ok()
                        .body(ApiResponse.DELETE("Address deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Address with id " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Error deleting user: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<?> getAddressByUserId(@PathVariable Long userId) {
        List<Address> response = addressService.getAddressByUserId(userId);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
