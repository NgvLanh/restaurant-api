package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.ZoneService;
import org.edu.restaurantapi.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    @Autowired
    private ZoneService service;

    @GetMapping
    public ResponseEntity<?> getAllZones(@RequestParam(value = "name", required = false) Optional<String> name,
                                  @RequestParam(value = "branch", required = false) Long branch, Pageable pageable) {
        var response = service.getAllZones(name, branch, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Zone request) {
//        var bse = service.findByName(request.getAddress());
//        if (bse) {
//            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Khu vực đã tồn tại"));
//        }
        var response = service.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Zone request) {
        var bse = service.findByNameAndIdNot(request.getAddress(), id);
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Khu vực đã tồn tại"));
        }
        var response = service.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

}
