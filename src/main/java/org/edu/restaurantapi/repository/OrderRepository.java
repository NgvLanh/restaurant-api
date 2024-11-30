package org.edu.restaurantapi.repository;


import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    Page<Order> findByIsDeleteFalseAndPaymentStatusTrue(Pageable pageable);

    Page<Order> findByIsDeleteFalseAndBranchIdAndPaymentStatusTrue(Long branchId, Pageable pageable);

    Page<Order> findByIsDeleteFalseAndTimeAndPaymentStatusTrue(Date time, Pageable pageable);

    Page<Order> findByIsDeleteFalseAndOrderStatusAndPaymentStatusTrue(OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByIsDeleteFalseAndBranchIdAndTimeAndPaymentStatusTrue(Long branchId, Date time, Pageable pageable);

    Page<Order> findByIsDeleteFalseAndBranchIdAndOrderStatusAndPaymentStatusTrue(Long branchId, OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByIsDeleteFalseAndTimeAndOrderStatusAndPaymentStatusTrue(Date time, OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByIsDeleteFalseAndBranchIdAndTimeAndOrderStatusAndPaymentStatusTrue(Long branchId, Date time, OrderStatus orderStatus, Pageable pageable);

//    Page<Order> findByIsDeleteFalse(Pageable pageable);

    @Query("SELECT COUNT(o) FROM orders o WHERE o.isDelete = false AND o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.PAID OR o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.DELIVERED")
    Long countTotalOrders();

    @Query("SELECT COUNT(o) FROM orders o WHERE o.isDelete = false AND o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.CANCELLED")
    Long countTotalOrdersCancelled();

    List<Order> findOrdersByBranchIdAndUserIdAndOrderStatusAndIsDeleteFalseAndPaymentStatusTrue(Long branchId, Long useId, OrderStatus orderStatus);

    List<Order> findOrdersByBranchIdAndUserIdAndPaymentStatusTrue(Long branchId, Long useId);

    List<Order> findOrdersByBranchIdAndOrderStatusAndTimeBetweenAndAddressIdIsNull(
            Long branchId, OrderStatus orderStatus, Date startTime, Date endTime);
}
