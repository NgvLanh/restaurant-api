package org.edu.restaurantapi.repository;


import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;

public interface OrderRepository extends JpaRepository<Order, Long> {


    Page<Order> findByIsDeleteFalseAndBranchIdAndTimeAndOrderStatus(Long branchId,
                                                                    Date time,
                                                                    OrderStatus orderStatus,
                                                                    Pageable pageable);

    Page<Order> findByIsDeleteFalse(Pageable pageable);
}
