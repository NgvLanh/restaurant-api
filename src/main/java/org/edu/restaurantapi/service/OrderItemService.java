package org.edu.restaurantapi.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.OrderItem;
import org.edu.restaurantapi.repository.DishRepository;
import org.edu.restaurantapi.repository.OrderItemRepository;
import org.edu.restaurantapi.repository.OrderRepository;
import org.edu.restaurantapi.request.OrderItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DishRepository dishRepository;

    public OrderItem createOrderItem(OrderItemRequest orderItem) {
        Dish dish = dishRepository.findById(orderItem.getDishId()).orElse(null);
        Order order = orderRepository.findById(orderItem.getOrderId()).orElse(null);

        OrderItem o = OrderItem.builder()
                .order(order)
                .dish(dish)
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .build();
        List<OrderItem> orderItemList = orderItemRepository.findOrderItemsByOrderId(order.getId());
        for (OrderItem existingOrderItem : orderItemList) {
            if (existingOrderItem.getDish().getId().equals(dish.getId())) {
               if (dish.getQuantity() > existingOrderItem.getQuantity()) {
                   existingOrderItem.setQuantity(existingOrderItem.getQuantity() + orderItem.getQuantity());
               }
                return orderItemRepository.save(existingOrderItem);
            }
        }

        return orderItemRepository.save(o);
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
