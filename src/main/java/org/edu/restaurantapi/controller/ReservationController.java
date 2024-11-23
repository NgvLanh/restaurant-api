package org.edu.restaurantapi.controller;

import jakarta.mail.MessagingException;
import org.edu.restaurantapi.request.ReserTableCancelRequest;
import org.edu.restaurantapi.request.ReserTableOffLineRequest;
import org.edu.restaurantapi.request.ReservationOnlineRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private ResponseEntity<?> createReservation(@RequestBody ReservationOnlineRequest request) throws MessagingException {
        var response = reservationService.createReservation(request);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

//    @PatchMapping("/cancel/{reservationId}/{reason}")
//    private ResponseEntity<?> cancelReservation(@PathVariable Long reservationId, @PathVariable String reason) throws MessagingException {
//        var response = reservationService.cancelReservation(reservationId,reason);
//        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
//    }

    @GetMapping("/cancel")
    private ResponseEntity<?> getAllCancelReservations(@RequestParam(value = "branch", required = false) String branch,
                                                       Pageable pageable) {
        var response = reservationService.getAllCancelReservations(branch, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping("/offline")
    private ResponseEntity<?> createReservationOffline(@RequestBody ReserTableOffLineRequest request) {
        LocalTime now = LocalTime.now();
        if (request.getStartTime().isBefore(now)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Thời gian đặt phải lớn hơn hiện tại"));
        }
        var response = reservationService.createReservationOffline(request);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/cancel")
    private ResponseEntity<?> cancelReservation(@RequestBody ReserTableCancelRequest request)  {
        var response = reservationService.cancelReservation(request);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

}
