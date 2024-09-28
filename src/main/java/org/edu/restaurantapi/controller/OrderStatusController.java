package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.OrderStatus;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-statuses")
public class OrderStatusController {

    @Autowired
    private OrderStatusService orderStatusService;

    @PostMapping
    public ResponseEntity<?> createOrderStatus(@RequestBody OrderStatus orderStatus) {
        if (orderStatusService.orderStatusNameExists(orderStatus.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.SERVER_ERROR("Order status name already exists"));
        }
        OrderStatus response = orderStatusService.createOrderStatus(orderStatus);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderStatusById(@PathVariable Long id) {
        Optional<OrderStatus> response = orderStatusService.getOrderStatusById(id);
        return response.map(orderStatus -> ResponseEntity.ok(ApiResponse.SUCCESS(orderStatus)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.SERVER_ERROR("Order status not found")));
    }

    @GetMapping
    public ResponseEntity<?> getAllOrderStatuses(Pageable pageable) {
        Page<OrderStatus> response = orderStatusService.getAllOrderStatuses(pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus updatedOrderStatus) {
        OrderStatus response = orderStatusService.updateOrderStatus(id, updatedOrderStatus);
        if (response != null) {
            return ResponseEntity.ok(ApiResponse.SUCCESS(response));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.SERVER_ERROR("Order status not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderStatus(@PathVariable Long id) {
        Boolean response = orderStatusService.deleteOrderStatus(id);
        if (response) {
            return ResponseEntity.ok(ApiResponse.SUCCESS("Order status deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.SERVER_ERROR("Order status not found or already deleted"));
    }
}
