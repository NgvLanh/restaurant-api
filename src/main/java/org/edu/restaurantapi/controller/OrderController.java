package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.OrderService;
import org.edu.restaurantapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestParam(value = "branchId", required = false)
                                          Optional<Long> branchId,
                                          @RequestParam(value = "time", required = false)
                                          Optional<Date> time,
                                          @RequestParam(value = "orderStatus", required = false)
                                          Optional<OrderStatus> orderStatus,
                                          Pageable pageable) {
        log.info(String.valueOf(branchId), time, orderStatus);
        var response = service.getAllOrders(branchId, time, orderStatus, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order request) {
        var response = service.createOrder(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Order request) {
//        var ose = service.findByNameAndIdNot(request.getName(), id);
//        if (ose) {
//            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên trạng thái đã tồn tại"));
//        }
        var response = service.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/total-order")
    public Long getTotalOrder() {
        return service.getTotalOrder();
    }

    @GetMapping("/total-order-cancelled")
    public Long getTotalOrderCancelled() {
        return service.getTotalOrderCancelled();
    }
}
