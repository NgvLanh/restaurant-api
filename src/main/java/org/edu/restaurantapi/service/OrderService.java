package org.edu.restaurantapi.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.*;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.repository.*;
import org.edu.restaurantapi.repository.OrderRepository;
import org.edu.restaurantapi.request.OrderManualRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TableRepository tableRepository;

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
        if (order.getOrderStatus() != OrderStatus.PENDING)
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
            case PENDING -> order.setOrderStatus(OrderStatus.CONFIRMED);
            case CONFIRMED -> order.setOrderStatus(OrderStatus.SHIPPED);
            case SHIPPED -> order.setOrderStatus(OrderStatus.DELIVERED);
            case DELIVERED -> order.setOrderStatus(OrderStatus.PAID);
            case PAID -> {
                return null;
            }

            // Các trạng thái liên quan đến món ăn
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

    public List<Order> getAllOrdersByUserId(Optional<Long> branchId, Optional<Long> userId, Optional<OrderStatus> orderStatus) {
        if (orderStatus.isPresent() && orderStatus.get() == OrderStatus.ALL) {
            return orderRepository.findOrdersByBranchIdAndUserId(branchId.get(), userId.get());
        }
        return orderRepository.findOrdersByBranchIdAndUserIdAndOrderStatus(branchId.get(), userId.get(), orderStatus.get());
    }

    public Order cancelOrder(Long orderId, Optional<String> reason) {
        return orderRepository.findById(orderId).map(b -> {
            if (b.getOrderStatus() == OrderStatus.PENDING) {
                b.setOrderStatus(OrderStatus.CANCELLED);
                b.setCancelReason(reason.get());
            } else {
                return null;
            }
            return orderRepository.save(b);
        }).orElse(null);
    }

    public List<Order> getAllOrdersWithTable(Optional<Long> branchId, Optional<String> date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = date.map(d -> LocalDate.parse(d, formatter))
                .orElseThrow(() -> new IllegalArgumentException("Bạn chưa cung cấp ngày"));

        Date startOfDay = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(localDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return orderRepository.findOrdersByBranchIdAndOrderStatusAndTimeBetweenAndAddressIdIsNull(
                branchId.get(),
                OrderStatus.READY_TO_SERVE,
                startOfDay,
                endOfDay
        );
    }


    public Order createOrderManual(OrderManualRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber()).orElse(null);
        request.getTable().setTableStatus(false);
        tableRepository.save(request.getTable());
        Order order = Order
                .builder()
                .branch(request.getBranch())
                .user(user)
                .fullName(request.getFullName())
                .table(request.getTable())
                .phoneNumber(request.getPhoneNumber())
                .isDelete(false)
                .total(0.0)
                .time(new Date())
                .orderStatus(OrderStatus.READY_TO_SERVE)
                .build();
        return orderRepository.save(order);
    }

    public Order updateServedOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        Table table = order.getTable();
        table.setTableStatus(true);
        tableRepository.save(table);
        if (order.getOrderStatus() == OrderStatus.READY_TO_SERVE) {
            order.setOrderStatus(OrderStatus.SERVED);
        }
        return orderRepository.save(order);
    }
}
