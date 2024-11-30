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
import org.edu.restaurantapi.request.OrderRequest;
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
    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;


    public Page<Order> getAllOrders(Optional<Long> branchId, Optional<Date> time,
                                    Optional<OrderStatus> orderStatus, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        if (branchId.isEmpty() && time.isEmpty() && orderStatus.isEmpty()) {
            return orderRepository.findByIsDeleteFalseAndPaymentStatusTrue(pageableSorted);
        }
        if (branchId.isPresent() && time.isPresent() && orderStatus.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndBranchIdAndTimeAndOrderStatusAndPaymentStatusTrue(
                    branchId.get(), time.get(), orderStatus.get(), pageableSorted);
        }
        if (branchId.isPresent() && orderStatus.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndBranchIdAndOrderStatusAndPaymentStatusTrue(
                    branchId.get(), orderStatus.get(), pageableSorted);
        }
        if (branchId.isPresent() && time.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndBranchIdAndTimeAndPaymentStatusTrue(
                    branchId.get(), time.get(), pageableSorted);
        }
        if (time.isPresent() && orderStatus.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndTimeAndOrderStatusAndPaymentStatusTrue(
                    time.get(), orderStatus.get(), pageableSorted);
        }
        if (branchId.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndBranchIdAndPaymentStatusTrue(branchId.get(), pageableSorted);
        }
        if (time.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndTimeAndPaymentStatusTrue(time.get(), pageableSorted);
        }
        if (orderStatus.isPresent()) {
            return orderRepository.findByIsDeleteFalseAndOrderStatusAndPaymentStatusTrue(orderStatus.get(), pageableSorted);
        }
        return orderRepository.findByIsDeleteFalseAndPaymentStatusTrue(pageableSorted);
    }


    public Order createOrder(OrderRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId()).orElse(null);
        User user = userRepository.findById(request.getUserId()).orElse(null);
        Address address = addressRepository.findById(request.getAddressId()).orElse(null);
        Optional<Discount> discount = discountRepository.findById(request.getDiscountId() == null ? 0 : request.getDiscountId());
        if (discount.isPresent()) {
            discount.get().setQuantity(discount.get().getQuantity() -1);
            discountRepository.save(discount.get());
        }

        Order requestOrder = Order.builder()
                .branch(branch)
                .address(address)
                .discount(discount.orElse(null))
                .user(user)
                .orderStatus(request.getOrderStatus())
                .total(request.getTotal())
                .paymentStatus(true)
                .build();
        Order response = orderRepository.save(requestOrder);
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
            Dish dish = dishRepository.findById(e.getDish().getId()).get();
            dish.setQuantity(dish.getQuantity() - e.getQuantity());
            dishRepository.save(dish);
            orderItemRepository.save(orderItem);
        });
        return response;
    }

    public Order cancelOrders(Long orderId) {
        var order = orderRepository.findById(orderId).orElse(null);
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            return null;
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderId(orderId);
        orderItems.forEach(e -> {
            Dish dish = dishRepository.findById(e.getDish().getId()).orElse(null);
            dish.setQuantity(dish.getQuantity() + e.getQuantity());
            dishRepository.save(dish);
        });
        return orderRepository.save(order);
    }

    public Order update(Long id) {
        var order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return null;
        }

        switch (order.getOrderStatus()) {
            case PENDING -> order.setOrderStatus(OrderStatus.CONFIRMED);
            case CONFIRMED -> order.setOrderStatus(OrderStatus.SHIPPED);
            case SHIPPED -> order.setOrderStatus(OrderStatus.DELIVERED);
            case DELIVERED -> {
                order.setOrderStatus(OrderStatus.PAID);
                order.setPaymentStatus(true);
                Invoice invoice = Invoice
                        .builder()
                        .order(order)
                        .branch(order.getBranch())
                        .total(order.getTotal())
                        .build();
                invoiceRepository.save(invoice);
            }
        }

        return orderRepository.save(order);
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
            return orderRepository.findOrdersByBranchIdAndUserIdAndPaymentStatusTrue(branchId.get(), userId.get());
        }
        return orderRepository.findOrdersByBranchIdAndUserIdAndOrderStatusAndIsDeleteFalseAndPaymentStatusTrue(branchId.get(), userId.get(), orderStatus.get());
    }

    public Order cancelOrder(Long orderId, Optional<String> reason) {
        Order order = orderRepository.findById(orderId).orElse(null);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(e -> {
            Dish dish = dishRepository.findById(e.getDish().getId()).get();
            dish.setQuantity(dish.getQuantity() + e.getQuantity());
            dishRepository.save(dish);
        });
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
//        request.getTable().setTableStatus(false);
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

    public Order updateServedOrder(Long id, Double total) {
        Order order = orderRepository.findById(id).get();
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderId(order.getId());
        orderItems.forEach(e -> {
            Dish dish = dishRepository.findById(e.getDish().getId()).orElse(null);
            dish.setQuantity(dish.getQuantity() - e.getQuantity());
            dishRepository.save(dish);
        });
        Table table = order.getTable();
//        table.setTableStatus(true);
        Reservation reservation = reservationRepository.findReservationsByOrderId(order.getId());
        reservation.setIsDelete(true);
        reservation.setEndTime(LocalTime.now());
        reservationRepository.save(reservation);
        tableRepository.save(table);
        if (order.getOrderStatus() == OrderStatus.READY_TO_SERVE) {
            order.setOrderStatus(OrderStatus.PAID);
            order.setPaymentStatus(true);
            order.setTotal(total);
            Invoice invoice = Invoice.builder()
                    .order(order)
                    .branch(order.getBranch())
                    .total(order.getTotal())
                    .build();
            invoiceRepository.save(invoice);
        }
        return orderRepository.save(order);
    }
}
