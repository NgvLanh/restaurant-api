package org.edu.restaurantapi.repository;


import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    Page<Order> findByPaymentStatusTrueAndActiveTrue(Pageable pageable);

    Page<Order> findByBranchIdAndPaymentStatusTrueAndActiveTrue(Long branchId, Pageable pageable);

    Page<Order> findByTimeAndPaymentStatusTrueAndActiveTrue(Date time, Pageable pageable);

    Page<Order> findByOrderStatusAndPaymentStatusTrueAndActiveTrue(OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByBranchIdAndTimeAndPaymentStatusTrueAndActiveTrue(Long branchId, Date time, Pageable pageable);

    Page<Order> findByBranchIdAndOrderStatusAndPaymentStatusTrueAndActiveTrue(Long branchId, OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByTimeAndOrderStatusAndPaymentStatusTrueAndActiveTrue(Date time, OrderStatus orderStatus, Pageable pageable);

    Page<Order> findByBranchIdAndTimeAndOrderStatusAndPaymentStatusTrueAndActiveTrue(Long branchId, Date time, OrderStatus orderStatus, Pageable pageable);

//    Page<Order> findByIsDeleteFalse(Pageable pageable);

    @Query("SELECT COUNT(o) FROM orders o WHERE o.active = true AND o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.PAID OR o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.DELIVERED")
    Long countTotalOrders();

    @Query("SELECT COUNT(o) FROM orders o WHERE o.active = true AND o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.CANCELLED")
    Long countTotalOrdersCancelled();

    List<Order> findOrdersByBranchIdAndUserIdAndOrderStatusAndPaymentStatusTrue(Long branchId, Long useId, OrderStatus orderStatus);

    List<Order> findOrdersByBranchIdAndUserIdAndPaymentStatusTrueAndActiveTrue(Long branchId, Long useId);

    List<Order> findOrdersByBranchIdAndOrderStatusAndTimeBetweenAndAddressIdIsNullAndActiveTrue(
            Long branchId, OrderStatus orderStatus, Date startTime, Date endTime);

    //
    List<Order> findOrdersByBranchIdAndUserIdAndActiveTrueOrderByIdDesc(Long branchId, Long useId);

    List<Order> findOrdersByBranchIdAndUserIdAndOrderStatusAndActiveTrueOrderByIdDesc(Long branchId, Long useId, OrderStatus orderStatus);

}
