package org.edu.restaurantapi.service;

import com.nimbusds.jose.proc.SecurityContext;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();
        var user = userRepository.findById(Long.parseLong(id)).orElse(null);
        user.setPassword(null);
        return user;
    }
}
