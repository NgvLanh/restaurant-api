package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table-reservations")
public class TableReservationController {

    @Autowired
    private TableReservationService service;

    @GetMapping
    private ResponseEntity<?> gets(@RequestParam(value = "branch", required = false) String branch, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.SUCCESS(service.gets(Long.parseLong(branch), pageable)));
    }
}
