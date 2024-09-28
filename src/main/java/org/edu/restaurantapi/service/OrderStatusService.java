package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.OrderStatus;
import org.edu.restaurantapi.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<OrderStatus> getAllOrderStatuses(Pageable pageable) {
        return orderStatusRepository.findOrderStatusByIsDeleteFalse(pageable);
    }

    public OrderStatus updateOrderStatus(Long id, OrderStatus updatedOrderStatus) {
        return orderStatusRepository.findById(id).map(existingOrderStatus -> {
            existingOrderStatus.setName(updatedOrderStatus.getName() != null
                    ? updatedOrderStatus.getName() : existingOrderStatus.getName());
            return orderStatusRepository.save(existingOrderStatus);
        }).orElse(null);
    }

    public Boolean deleteOrderStatus(Long id) {
        return orderStatusRepository.findById(id).map(orderStatus -> {
            orderStatus.setIsDelete(true);
            orderStatusRepository.save(orderStatus);
            return true;
        }).orElse(false);
    }

    public boolean orderStatusNameExists(String name) {
        return orderStatusRepository.existsByName(name);
    }
}
