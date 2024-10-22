package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    private ResponseEntity<?> createCart(@RequestBody Cart cart) {
        Cart response = cartService.createCart(cart);
        if (response != null) {
            return ResponseEntity.ok(ApiResponse.SUCCESS(response));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.SERVER_ERROR("Created cart failed"));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteCart(@PathVariable Long id) {
        boolean isDeleted = cartService.deleteCart(id);
        if (isDeleted) {
            return ResponseEntity.ok(ApiResponse.SUCCESS("Cart deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.SERVER_ERROR("Failed to delete cart"));
    }

    @GetMapping("/{cartId}")
    private ResponseEntity<?> getCarts(@PathVariable Long cartId) {
        var response = cartService.getCarts(cartId);
        if (response != null) {
            return ResponseEntity.ok(ApiResponse.SUCCESS(response));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.SERVER_ERROR("Lỗi lấy danh sách sản phầm trong giỏ hàng."));
    }

}
