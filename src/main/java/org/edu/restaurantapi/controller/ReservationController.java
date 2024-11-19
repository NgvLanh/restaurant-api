package org.edu.restaurantapi.controller;

import jakarta.mail.MessagingException;
import org.edu.restaurantapi.model.Reservation;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    private ResponseEntity<?> getAllReservations(@RequestParam(value = "branch", required = false) String branch) {
        var response = reservationService.getAllReservations(branch);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    private ResponseEntity<?> createReservation(@RequestBody Reservation request) throws MessagingException {
        var response = reservationService.createReservation(request);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/cancel/{reservationId}/{reason}")
    private ResponseEntity<?> cancelReservation(@PathVariable Long reservationId, @PathVariable String reason) throws MessagingException {
        var response = reservationService.cancelReservation(reservationId,reason);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }
}
