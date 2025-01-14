package org.edu.restaurantapi.controller;

import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.OrderItem;
import org.edu.restaurantapi.repository.DishRepository;
import org.edu.restaurantapi.repository.OrderItemRepository;
import org.edu.restaurantapi.request.OrderItemRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItemRequest orderItem) {
        OrderItem response = orderItemService.createOrderItem(orderItem);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{orderItemId}/{quantity}")
    public ResponseEntity<?> updateQuantityOrderItem(@PathVariable Long orderItemId,
                                                     @PathVariable Integer quantity) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        Dish dish = dishRepository.findById(orderItem.getDish().getId()).get();
        if (dish.getQuantity() < quantity) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Món ăn này chỉ còn: " + dish.getQuantity()));
        }
        OrderItem response = orderItemService.updateQuantityOrderItem(orderItemId, quantity);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.ok(ApiResponse.SUCCESS("Xoá thành công"));
    }
}
