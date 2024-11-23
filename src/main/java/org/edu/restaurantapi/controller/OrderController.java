package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.request.OrderManualRequest;
import org.edu.restaurantapi.request.OrderRequest;
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

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestParam(value = "branchId", required = false)
                                          Optional<Long> branchId,
                                          @RequestParam(value = "time", required = false)
                                          Optional<Date> time,
                                          @RequestParam(value = "orderStatus", required = false)
                                          Optional<OrderStatus> orderStatus,
                                          Pageable pageable) {
        var response = orderService.getAllOrders(branchId, time, orderStatus, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        var response = orderService.createOrder(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PostMapping("/manual")
    public ResponseEntity<?> createOrderManual(@Valid @RequestBody OrderManualRequest request) {
        var response = orderService.createOrderManual(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        var response = orderService.update(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrders(@PathVariable Long id) {
        var orders = orderService.cancelOrders(id);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = orderService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/total-order")
    public Long getTotalOrder() {
        return orderService.getTotalOrder();
    }

    @GetMapping("/total-order-cancelled")
    public Long getTotalOrderCancelled() {
        return orderService.getTotalOrderCancelled();
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAllOrdersByUserId(@RequestParam(value = "branchId", required = false)
                                          Optional<Long> branchId,
                                          @RequestParam(value = "userId", required = false)
                                          Optional<Long> userId,
                                          @RequestParam(value = "orderStatus", required = false)
                                          Optional<OrderStatus> orderStatus) {
        var response = orderService.getAllOrdersByUserId(branchId, userId, orderStatus);
        response.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId,
                                         @RequestParam(value = "reason", required = false)
                                        Optional<String> reason) {
        var response = orderService.cancelOrder(orderId, reason);
        if (response==null) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Đơn hàng đã được xác nhận trước đó"));
        }
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/table")
    public ResponseEntity<?> getAllOrdersWithTable(@RequestParam(value = "branchId", required = false)
                                          Optional<Long> branchId,
                                          @RequestParam(value = "date", required = false)
                                          Optional<String> date) {
        var response = orderService.getAllOrdersWithTable(branchId, date);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/served/{id}/{total}")
    public ResponseEntity<?> updateServedOrder(@PathVariable Long id, @PathVariable Double total) {
        var response = orderService.updateServedOrder(id, total);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
