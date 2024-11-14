package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.CartItem;
import org.edu.restaurantapi.repository.CartItemRepository;
import org.edu.restaurantapi.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;

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

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    public Optional<CartItem> findByCartIdAndDishId(Long id, Long id1) {
        return cartItemRepository.findByCartIdAndDishId(id, id1);
    }

    public List<CartItem> findByCartItemsByCartId(Long cartId) {
        return cartItemRepository.findCartItemByCartId(cartId);
    }

    public Optional<CartItem> updateQuantityCartItem(Long cartItemId, Integer quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        cartItem.ifPresent(e -> e.setQuantity(quantity));
        return cartItem.map(cartItemRepository::save);
    }

    public List<CartItem> updateSelectAllCartItem(Long userId, Boolean status) {
        Optional<Cart> cart = cartRepository.findCartByUserId(userId);
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
}
