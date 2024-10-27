package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.OrderRepository;
import org.edu.restaurantapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public Page<Order> gets(String user, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return repository.findByIsDeleteFalseAndUserId(Long.parseLong(user), pageableSorted);
    }

    public Order create(Order request) {
        return repository.save(request);
    }

    public Order update(Long id, Order request) {
        return repository.findById(id).map(b -> {
            return repository.save(b);
        }).orElse(null);
    }

    public Boolean delete(Long id) {
        return repository.findById(id).map(o -> {
            o.setIsDelete(true);
            return true;
        }).orElse(false);
    }

//    public Boolean findByName(String name) {
//        return repository.findByName(name) != null;
//    }
//
//    public Boolean findByNameAndIdNot(String name, Long id) {
//        return repository.findByNameAndIdNot(name, id) != null;
//    }
}
