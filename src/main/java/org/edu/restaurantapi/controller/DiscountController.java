package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi._enum.DiscountMethod;
import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService service;

    @GetMapping
    public ResponseEntity<?> gets(@RequestParam(value = "code", required = false) String code,
                                  Pageable pageable) {
<<<<<<< Updated upstream
        var response = service.gets(code, pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
=======
        var response = service.getAllDiscountsByBranchId(branchId, pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

>>>>>>> Stashed changes

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Discount request) throws URISyntaxException, IOException {
        if (service.checkDiscountCode(request.getCode())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã giảm giá đã tồn tại"));
        }
        var response = service.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Discount request) throws IOException {
        if (service.findByCodeAndIdNot(request.getCode(), id)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã giảm giá đã tồn tại"));
        }
        var response = service.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/countMonth")
    public ResponseEntity<?> getDiscountData(Pageable pageable) {
        var response = service.getDiscountStatsByMonth(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

}
