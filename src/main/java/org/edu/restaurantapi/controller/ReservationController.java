package org.edu.restaurantapi.controller;

import jakarta.mail.MessagingException;
import org.edu.restaurantapi.model.Reservation;
import org.edu.restaurantapi.model.TableReservation;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @GetMapping
    private ResponseEntity<?> gets(@RequestParam(value = "branch", required = false) String branch, Pageable pageable) {
        var response = service.gets(branch, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    private ResponseEntity<?> create(@RequestBody Reservation request) throws MessagingException {
        var response = service.create(request);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }
}
