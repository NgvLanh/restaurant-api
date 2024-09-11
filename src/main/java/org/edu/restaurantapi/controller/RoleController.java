package org.edu.restaurantapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Role;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    private ResponseEntity<?> createRole(@RequestBody Role role) {
        Role response = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.CREATED(response));
    }

    @GetMapping
    private ResponseEntity<?> getRoles() {
        List<Role> response = roleService.getRoles();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.SUCCESS(response));
    }

}
