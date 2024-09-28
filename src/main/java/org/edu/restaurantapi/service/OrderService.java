package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findOrdersByIsDeleteFalse(pageable);
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id).map(existingOrder -> {
            existingOrder.setOrderStatus(updatedOrder.getOrderStatus()
                    != null ? updatedOrder.getOrderStatus() : existingOrder.getOrderStatus());
            existingOrder.setUser(updatedOrder.getUser()
                    != null ? updatedOrder.getUser() : existingOrder.getUser());
            existingOrder.setTable(updatedOrder.getTable()
                    != null ? updatedOrder.getTable() : existingOrder.getTable());
            existingOrder.setAddress(updatedOrder.getAddress()
                    != null ? updatedOrder.getAddress() : existingOrder.getAddress());
            existingOrder.setDiscount(updatedOrder.getDiscount()
                    != null ? updatedOrder.getDiscount() : existingOrder.getDiscount());
            existingOrder.setIsDelete(updatedOrder.getIsDelete()
                    != null ? updatedOrder.getIsDelete() : existingOrder.getIsDelete());
            return orderRepository.save(existingOrder);
        }).orElse(null);
    }

    public Boolean deleteOrder(Long id) {
        return orderRepository.findById(id).map(order -> {
            order.setIsDelete(true);
            orderRepository.save(order);
            return true;
        }).orElse(false);
    }
}
