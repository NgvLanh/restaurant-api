package org.edu.restaurantapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.dto.AsyncDish;
import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.CartItemService;
import org.edu.restaurantapi.service.CartService;
import org.edu.restaurantapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

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


    @PostMapping("/async/{userId}")
    private ResponseEntity<?> asyncCart(@RequestBody CartItem[] request,
                                        @PathVariable Long userId) {
        Optional<Cart> cartExists = cartService.findByCartUserId(userId);
        Optional<User> userExist = userService.findUserById(userId);

        Cart cart;
        if (!cartExists.isPresent()) {
            cart = Cart.builder().user(userExist.get()).build();
            cartService.createCart(cart);
        } else {
            cart = cartExists.get();
        }

        for (CartItem cartItemRequest : request) {
            Optional<CartItem> existingCartItem = cartItemService.findByCartIdAndDishId(cart.getId(), cartItemRequest.getDish().getId());
            if (existingCartItem.isPresent()) {
                CartItem cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
                cartItemService.createCartItem(cartItem);
            } else {
                cartItemRequest.setCart(cart);
                cartItemService.createCartItem(cartItemRequest);
            }
        }

        return ResponseEntity.ok().body(ApiResponse.SUCCESS(request));
    }

}
