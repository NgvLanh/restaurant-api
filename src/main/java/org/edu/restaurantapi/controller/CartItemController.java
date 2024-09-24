package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.CartItem;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<?> createCartItem(@RequestBody CartItem cartItem) {
        CartItem response = cartItemService.createCartItem(cartItem);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id, @RequestBody CartItem updatedCartItem) {
        CartItem response = cartItemService.updateCartItem(id, updatedCartItem);
        if (response != null) {
            return ResponseEntity.ok(ApiResponse.SUCCESS(response));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.SERVER_ERROR("CartItem not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok(ApiResponse.SUCCESS("CartItem deleted successfully"));
    }
}
