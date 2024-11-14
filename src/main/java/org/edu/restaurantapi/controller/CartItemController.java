package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.CartItemService;
import org.edu.restaurantapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartService cartService;

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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.SERVER_ERROR("CartItem not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok(ApiResponse.SUCCESS("Xoá sp #:" + id + " thành công"));
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<?> getCartItemsByUserId(@PathVariable Long userId) {
        Cart cart = cartService.findByCartUserId(userId).get();
        List<CartItem> cartItems = cartItemService.findByCartItemsByCartId(cart.getId());
        cartItems.forEach(cartItem -> {
            cartItem.getDish().setImage("http://localhost:8080/api/files/" + cartItem.getDish().getImage());
        });
        return ResponseEntity.ok(ApiResponse.SUCCESS(cartItems));
    }


    @PatchMapping("/{cartItemId}/{quantity}")
    public ResponseEntity<?> updateQuantityCartItem(@PathVariable Long cartItemId, @PathVariable Integer quantity) {
        var response = cartItemService.updateQuantityCartItem(cartItemId, quantity);
        if (response.isPresent()) {
            return ResponseEntity.ok(ApiResponse.SUCCESS(response));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.NOT_FOUND("Không tìm thấy món ăn này"));
    }

    @PatchMapping("/cart/{userId}/{status}")
    public ResponseEntity<?> updateSelectAll(@PathVariable Long userId, @PathVariable Boolean status) {
        var response = cartItemService.updateSelectAllCartItem(userId, status);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/status/{cartItemId}")
    public ResponseEntity<?> updateSelectStatus(@PathVariable Long cartItemId) {
        var response = cartItemService.updateSelectStatusCartItem(cartItemId);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
