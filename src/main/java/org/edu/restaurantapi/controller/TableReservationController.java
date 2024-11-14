package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/table-reservations")
public class TableReservationController {

    @Autowired
    private TableReservationService service;

    @GetMapping
    private ResponseEntity<?> gets(@RequestParam(value = "branch", required = false) String branch, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.SUCCESS(service.gets(Long.parseLong(branch), pageable)));
    }
//    @GetMapping("/countTable")
//    public ResponseEntity<?> getReservation(Pageable pageable) {
//        var response = service.getCountTableReservation(pageable);
//        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
//    }
    @GetMapping("countTable")
    public ResponseEntity<?> getCountUser(Pageable pageable) {
         var response = service.getReservationsByWeekday(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

}
