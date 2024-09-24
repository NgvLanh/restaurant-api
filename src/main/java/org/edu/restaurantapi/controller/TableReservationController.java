package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Address;
import org.edu.restaurantapi.model.TableReservation;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table-reservations")
public class TableReservationController {

    @Autowired
    private TableReservationService tableReservationService;
    @PostMapping
    private ResponseEntity<?> tableReservation(@Valid @RequestBody TableReservation tableReservation) {
        try {
            TableReservation response = tableReservationService.createTableReservation(tableReservation);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.CREATED(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Created TableReservation failed: " + e.getMessage()));
        }

    }

    @GetMapping
    private ResponseEntity<?> getTableReservation(Pageable pageable) {
        Page<TableReservation> response = tableReservationService.getTableReservation(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
