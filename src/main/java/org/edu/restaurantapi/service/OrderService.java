package org.edu.restaurantapi.service;

import org.aspectj.weaver.ast.Or;
import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.*;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.repository.*;
import org.edu.restaurantapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    public Page<Order> getAllOrders(Optional<Long> branchId, Optional<Date> time,
                                    Optional<OrderStatus> orderStatus, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        if (branchId.isEmpty() || time.isEmpty() || orderStatus.isEmpty()) {
            return orderRepository.findByIsDeleteFalse(pageableSorted);
        }
        return orderRepository.findByIsDeleteFalseAndBranchIdAndTimeAndOrderStatus(branchId.get(), time.get(), orderStatus.get(), pageableSorted);
    }

    public Order createOrder(Order request) {
        Order response = orderRepository.save(request);
        User user = request.getUser();
        Optional<Cart> cart = cartRepository.findCartByUserId(user.getId());
        List<CartItem> items = cartItemRepository.findCartItemByCartId(cart.get().getId());
        items.forEach(e -> {
            OrderItem orderItem = OrderItem
                    .builder()
                    .order(response)
                    .dish(e.getDish())
                    .price(e.getDish().getPrice())
                    .quantity(e.getQuantity())
                    .build();
            orderItemRepository.save(orderItem);
        });
        return response;
    }

    public Order update(Long id, Order request) {
        return orderRepository.findById(id).map(b -> {
            return orderRepository.save(b);
        }).orElse(null);
    }

    public Boolean delete(Long id) {
        return orderRepository.findById(id).map(o -> {
            o.setIsDelete(true);
            return true;
        }).orElse(false);
    }

    public Long getTotalOrder() {
        return orderRepository.countTotalOrders();
    }

    public void deleteOrder(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(orderId));
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderId(order.getId());
            if (!orderItems.isEmpty()) {
                orderItemRepository.deleteAll(orderItems);
            }
            orderRepository.delete(order);
        } else {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
    }

    public Optional<Order> findById(long l) {
        return orderRepository.findById(l);
    }
}
