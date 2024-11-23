package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.CartItemRepository;
import org.edu.restaurantapi.repository.DishRepository;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.CartItemService;
import org.edu.restaurantapi.service.CartService;
import org.edu.restaurantapi.service.DishService;
import org.edu.restaurantapi.service.UserService;
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
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

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

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<?> deleteCartItemsByCartId(@PathVariable Long id) {
        Optional<Cart> cart = cartService.findByCartUserId(id);
        cartItemService.deleteCartItemsByCartId(cart.get().getId());
        return ResponseEntity.ok(ApiResponse.SUCCESS("Xoá sp #:" + id + " thành công"));
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<?> getCartItemsByUserId(@PathVariable Long userId) {
        Optional<Cart> cart = cartService.findByCartUserId(userId);
        List<CartItem> cartItems = cartItemService.findByCartItemsByCartId(cart.get().getId());
        cartItems.forEach(cartItem -> {
            cartItem.getDish().setImage("http://localhost:8080/api/files/" + cartItem.getDish().getImage());
        });
        return ResponseEntity.ok(ApiResponse.SUCCESS(cartItems));
    }


    @PatchMapping("/{cartItemId}/{quantity}")
    public ResponseEntity<?> updateQuantityCartItem(@PathVariable Long cartItemId, @PathVariable Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        Dish dish = dishRepository.findById(cartItem.getDish().getId()).orElse(null);
        if (dish.getQuantity() < quantity) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số lương món ăn này chỉ còn: " + dish.getQuantity()));
        }
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

    @PostMapping("/{userId}/{dishId}/{quantity}")
    public ResponseEntity<?> addDishToCart(@PathVariable Long userId, @PathVariable Long dishId,
                                           @PathVariable Integer quantity) {
        Dish dish = dishRepository.findById(dishId).orElse(null);
        if (dish.getQuantity() == 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Món này đã hết hàng"));
        } else if (dish.getQuantity() < quantity) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số lương món ăn này chỉ còn: " + dish.getQuantity()));
        }
        var response = cartItemService.addDishToCart(userId, dishId, quantity);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok(ApiResponse.SUCCESS("Xoá sp #:" + id + " thành công"));
    }
}
