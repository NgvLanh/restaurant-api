package org.edu.restaurantapi.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.repository.CartItemRepository;
import org.edu.restaurantapi.repository.CartRepository;
import org.edu.restaurantapi.repository.DishRepository;
import org.edu.restaurantapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private UserRepository userRepository;

    public CartItem createCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(Long id, CartItem updatedCartItem) {
        return cartItemRepository.findById(id).map(existingCartItem -> {
            existingCartItem.setQuantity(updatedCartItem.getQuantity()
                    != null ? updatedCartItem.getQuantity() : existingCartItem.getQuantity());
            existingCartItem.setStatus(updatedCartItem.getStatus()
                    != null ? updatedCartItem.getStatus() : existingCartItem.getStatus());
            return cartItemRepository.save(existingCartItem);
        }).orElse(null);
    }

    @Transactional
    public void deleteCartItemsByCartId(Long id) {
        cartItemRepository.deleteCartItemsByCartIdAndStatusTrue(id);
    }

    public Optional<CartItem> findByCartIdAndDishId(Long id, Long id1) {
        return cartItemRepository.findByCartIdAndDishId(id, id1);
    }

    public List<CartItem> findByCartItemsByCartId(Long cartId) {
        return cartItemRepository.findCartItemByCartId(cartId);
    }

    public Optional<CartItem> updateQuantityCartItem(Long cartItemId, Integer quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.get().getDish().getQuantity() >= quantity) {
            cartItem.ifPresent(e -> e.setQuantity(quantity));
        }
        return cartItem.map(cartItemRepository::save);
    }

    public List<CartItem> updateSelectAllCartItem(Long userId, Boolean status) {
        Optional<Cart> cart = cartRepository.findCartByUserIdAndActiveTrue(userId);
        List<CartItem> cartItem = cartItemRepository.findCartItemByCartId(cart.get().getId());
        cartItem.forEach(e->{
            e.setStatus(status);
            cartItemRepository.save(e);
        });
        return cartItem;
    }

    public CartItem updateSelectStatusCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .map(cartItem -> {
                    cartItem.setStatus(!cartItem.getStatus());
                    return cartItemRepository.save(cartItem);
                })
                .orElse(null);
    }

    public CartItem addDishToCart(Long userId, Long dishId, Integer quantity) {
        Optional<Cart> cart = cartRepository.findCartByUserIdAndActiveTrue(userId);
        Optional<Dish> dish = dishRepository.findById(dishId);
        List<CartItem> cartItems = cartItemRepository.findCartItemByCartId(cart.get().getId());
        for (CartItem existingCartItem : cartItems) {
            if (existingCartItem.getDish().getId().equals(dishId)) {
                if (dish.get().getQuantity() > existingCartItem.getQuantity() + quantity) {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                }
                return cartItemRepository.save(existingCartItem);
            }
        }
        CartItem cartItem = CartItem
                .builder()
                .cart(cart.get())
                .dish(dish.get())
                .status(false)
                .quantity(quantity)
                .build();
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}
