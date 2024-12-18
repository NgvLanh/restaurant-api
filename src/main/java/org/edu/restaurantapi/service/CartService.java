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
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public boolean deleteCart(Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CartItem> getCarts(Long cartId) {
        return cartItemRepository.findCartItemByCartId(cartId);
    }

    public Optional<Cart> findByCartUserId(Long userId) {
        return cartRepository.findCartByUserIdAndActiveTrue(userId);
    }
}
