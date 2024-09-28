package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        Order response = orderService.createOrder(order);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Optional<Order> response = orderService.getOrderById(id);
        return response.map(order -> ResponseEntity.ok(ApiResponse.SUCCESS(order)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.SERVER_ERROR("Order not found")));
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(Pageable pageable) {
        Page<Order> response = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        Order response = orderService.updateOrder(id, updatedOrder);
        if (response != null) {
            return ResponseEntity.ok(ApiResponse.SUCCESS(response));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.SERVER_ERROR("Order not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        Boolean response = orderService.deleteOrder(id);
        if (response) {
            return ResponseEntity.ok(ApiResponse.SUCCESS("Order deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.SERVER_ERROR("Order not found or already deleted"));
    }
}
