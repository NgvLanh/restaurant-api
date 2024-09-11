package org.edu.restaurantapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Role;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/info")
    private ResponseEntity<?> getUserInfo() {
        User response = userService.getUserInfo();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.SUCCESS(response));
    }

//    @PostMapping
//    private ResponseEntity<?> createRole(@RequestBody Role role) {
//        Role response = userService.createUser(role);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.CREATED(response));
//    }

//    @GetMapping
//    private ResponseEntity<?> getRoles() {
//        List<Role> response = userService.getRoles();
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(ApiResponse.SUCCESS(response));
//    }

}
