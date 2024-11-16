package org.edu.restaurantapi.repository;


import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface OrderRepository extends JpaRepository<Order, Long> {


    Page<Order> findByIsDeleteFalseAndBranchIdAndTimeAndOrderStatus(Long branchId,
                                                                    Date time,
                                                                    OrderStatus orderStatus,
                                                                    Pageable pageable);

    Page<Order> findByIsDeleteFalse(Pageable pageable);

    @Query("SELECT COUNT(o) FROM orders o WHERE o.isDelete = false AND o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.PAID OR o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.DELIVERED")
    Long countTotalOrders();

    @Query("SELECT COUNT(o) FROM orders o WHERE o.isDelete = false AND o.orderStatus = org.edu.restaurantapi._enum.OrderStatus.CANCELLED")
    Long countTotalOrdersCancelled();
}
