package org.edu.restaurantapi.repository;


import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o" +
            " FROM orders o" +
            " WHERE o.isDelete = false" +
            " AND (:user = 0 OR o.user.id = :user)")
    Page<Order> findByIsDeleteFalseAndUserId(Long user, Pageable pageable);

    Order findByIsDeleteFalseAndUser(User user);
}
