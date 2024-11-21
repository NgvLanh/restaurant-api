package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.OrderItem;
import org.edu.restaurantapi.repository.OrderItemRepository;
import org.edu.restaurantapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    public OrderItem createOrderItem(OrderItem orderItem) {
        Dish dishExists = orderItem.getDish();
        Order order = orderItem.getOrder();
        List<OrderItem> orderItemList = orderItemRepository.findOrderItemsByOrderId(order.getId());
        for (OrderItem existingOrderItem : orderItemList) {
            if (existingOrderItem.getDish().getId().equals(dishExists.getId())) {
                existingOrderItem.setQuantity(existingOrderItem.getQuantity() + 1);
                return orderItemRepository.save(existingOrderItem);
            }
        }

        return orderItemRepository.save(orderItem);
    }


    public OrderItem updateQuantityOrderItem(Long orderItemId, Integer quantity) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);
        OrderItem existingOrderItem = orderItem.get();
        existingOrderItem.setQuantity(quantity);
        return orderItemRepository.save(existingOrderItem);
    }

    public void deleteOrderItem(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
}
