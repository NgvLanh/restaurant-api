package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.OrderStatus;
import org.edu.restaurantapi.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public OrderStatus createOrderStatus(OrderStatus orderStatus) {
        return orderStatusRepository.save(orderStatus);
    }

    public Optional<OrderStatus> getOrderStatusById(Long id) {
        return orderStatusRepository.findById(id);
    }

    public List<OrderStatus> getAllOrderStatuses() {
        return orderStatusRepository.findAll();
    }

    public OrderStatus updateOrderStatus(Long id, OrderStatus updatedOrderStatus) {
        return orderStatusRepository.findById(id).map(existingOrderStatus -> {
            existingOrderStatus.setName(updatedOrderStatus.getName() != null
                    ? updatedOrderStatus.getName() : existingOrderStatus.getName());
            return orderStatusRepository.save(existingOrderStatus);
        }).orElse(null);
    }

    public OrderStatus deleteOrderStatus(Long id) {
        OrderStatus orderStatus = orderStatusRepository.findById(id).orElse(null);
        if (orderStatus == null || orderStatus.getIsDelete()) {
            return null;
        }
        orderStatus.setIsDelete(true);
        orderStatusRepository.save(orderStatus);
        return orderStatus;
    }

    public boolean orderStatusNameExists(String name) {
        return orderStatusRepository.existsByName(name);
    }
}
