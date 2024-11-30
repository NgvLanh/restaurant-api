package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi._enum.DiscountMethod;
import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.request.DiscountRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public ResponseEntity<?> getAllDiscountsByBranchIdAndMonth(
            @RequestParam(value = "branchId", required = false) Long branchId,
            @RequestParam(value = "month", required = false) String month,
            Pageable pageable) {
        var response = discountService.getAllDiscountsByBranchIdAndMonth(branchId, month, pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> checkDiscountCode(@PathVariable String code) {
        Discount response = discountService.checkDiscountCode(code);
        if (response == null) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã giảm giá không chính xác"));
        } else if (response.getEndDate().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã giảm giá đã hết hạn"));
        } else if (response.getQuantity() < 1) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã giảm giá đã hết"));
        }
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }


    @PostMapping
    public ResponseEntity<?> createDiscount(@Valid @RequestBody DiscountRequest request) {
        Discount discount = discountService.checkDiscountCode(request.getCode());
        if (discount != null) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã giảm giá đã tồn tại"));
        }
        var response = discountService.createDiscount(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Discount request) throws IOException {
        if (discountService.findByCodeAndIdNot(request.getCode(), id)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã giảm giá đã tồn tại"));
        }
        var response = discountService.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = discountService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/countMonth")
    public ResponseEntity<?> getDiscountData(Pageable pageable) {
        var response = discountService.getDiscountStatsByMonth(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

}
