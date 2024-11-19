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
        // Sắp xếp theo id giảm dần (mới nhất lên đầu)
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));

        // Nếu tất cả các tham số đều không có (branchId, time, orderStatus), trả về tất cả đơn hàng chưa xóa
        if (branchId.isEmpty() && time.isEmpty() && orderStatus.isEmpty()) {
            return orderRepository.findByIsDeleteFalse(pageableSorted);
        }

        // Nếu có đầy đủ 3 tham số lọc: branchId, time, và orderStatus
        if (branchId.isPresent() && time.isPresent() && orderStatus.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndBranchIdAndTimeAndOrderStatus(
                    branchId.get(), time.get(), orderStatus.get(), pageableSorted);
        }

        // Nếu chỉ có branchId và orderStatus
        if (branchId.isPresent() && orderStatus.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndBranchIdAndOrderStatus(
                    branchId.get(), orderStatus.get(), pageableSorted);
        }

        // Nếu chỉ có branchId và time
        if (branchId.isPresent() && time.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndBranchIdAndTime(
                    branchId.get(), time.get(), pageableSorted);
        }

        // Nếu chỉ có time và orderStatus
        if (time.isPresent() && orderStatus.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndTimeAndOrderStatus(
                    time.get(), orderStatus.get(), pageableSorted);
        }

        // Nếu chỉ có branchId
        if (branchId.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndBranchId(branchId.get(), pageableSorted);
        }

        // Nếu chỉ có time
        if (time.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndTime(time.get(), pageableSorted);
        }

        // Nếu chỉ có orderStatus
        if (orderStatus.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndOrderStatus(orderStatus.get(), pageableSorted);
        }

        // Trường hợp cuối cùng: trả về tất cả đơn hàng chưa xóa nếu không có tham số lọc nào
        return orderRepository.findByIsDeleteFalse(pageableSorted);
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

    public Order cancelOrders(Long orderId) {
        var order = orderRepository.findById(orderId).orElse(null);
        if (order.getOrderStatus() != OrderStatus.PENDING_CONFIRMATION)
            return null;
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Order update(Long id, Order request) {
        var order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return null;
        }

        switch (order.getOrderStatus()) {
            // Các trạng thái liên quan đến giao hàng
            case PENDING_CONFIRMATION -> order.setOrderStatus(OrderStatus.CONFIRMED);
            case CONFIRMED -> order.setOrderStatus(OrderStatus.DELIVERY);
            case DELIVERY -> order.setOrderStatus(OrderStatus.DELIVERED);
            case DELIVERED -> order.setOrderStatus(OrderStatus.PAID);
            case PAID -> {
                return null;
            }

            // Các trạng thái liên quan đến món ăn

            case ORDERED -> order.setOrderStatus(OrderStatus.IN_KITCHEN);
            case IN_KITCHEN -> order.setOrderStatus(OrderStatus.READY_TO_SERVE);
            case READY_TO_SERVE -> order.setOrderStatus(OrderStatus.SERVED);
            case SERVED -> order.setOrderStatus(OrderStatus.PAID);
            case CANCELLED -> {
                return null;
            }


            default -> throw new IllegalStateException("Unexpected status: " + order.getOrderStatus());
        }

        return orderRepository.save(order);  // Lưu lại đơn hàng đã được cập nhật
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

    public Long getTotalOrderCancelled() {
        return orderRepository.countTotalOrdersCancelled();
    }
}
