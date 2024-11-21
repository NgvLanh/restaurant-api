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


    public Page<Order> findByIsDeleteFalse(Pageable pageable);

    public Page<Order> findByIsDeleteFalseAndBranchId(Long branchId, Pageable pageable);

    public Page<Order> findByIsDeleteFalseAndTime(Date time, Pageable pageable);

    public Page<Order> findByIsDeleteFalseAndOrderStatus(OrderStatus orderStatus, Pageable pageable);

    public Page<Order> findByIsDeleteFalseAndBranchIdAndTime(Long branchId, Date time, Pageable pageable);

    public Page<Order> findByIsDeleteFalseAndBranchIdAndOrderStatus(Long branchId, OrderStatus orderStatus, Pageable pageable);

    public Page<Order> findByIsDeleteFalseAndTimeAndOrderStatus(Date time, OrderStatus orderStatus, Pageable pageable);

    public Page<Order> findByIsDeleteFalseAndBranchIdAndTimeAndOrderStatus(Long branchId, Date time, OrderStatus orderStatus, Pageable pageable);

//    Page<Order> findByIsDeleteFalse(Pageable pageable);

    @Query("SELECT COUNT(o) FROM orders o WHERE o.isDelete = false AND o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.PAID OR o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.DELIVERED")
    Long countTotalOrders();

    @Query("SELECT COUNT(o) FROM orders o WHERE o.isDelete = false AND o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.CANCELLED")
    Long countTotalOrdersCancelled();

    List<Order> findOrdersByBranchIdAndUserIdAndOrderStatus(Long branchId, Long useId, OrderStatus orderStatus);

    List<Order> findOrdersByBranchIdAndUserId(Long branchId, Long useId);

    List<Order> findOrdersByBranchIdAndOrderStatusAndTimeBetweenAndAddressIdIsNull(
            Long branchId, OrderStatus orderStatus, Date startTime, Date endTime);
}
