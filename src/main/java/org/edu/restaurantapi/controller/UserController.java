package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // đưa token sẽ trả về đúng đó là ai
    @PostMapping("/info")
    private ResponseEntity<?> getUserInfo() {
        User response = userService.getUserInfo();
        response.setPassword(null);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    private ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (userService.userEmailExists(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.BAD_REQUEST("Email này đã tồn tại"));
        } else if (userService.userPhoneNumberExists(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.BAD_REQUEST("Số điện thoại này đã tồn tại"));
        } else {
            try {
                User response = userService.createUser(user);
                response.setPassword(null);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.CREATED(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR(e.getMessage()));
            }
        }
    }

    @PatchMapping("/{id}")
    private ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User response = userService.updateUser(id, user);
            response.setPassword(null);
            return ResponseEntity.ok()
                    .body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR(e.getMessage()));
        }
    }

    @GetMapping
    private ResponseEntity<?> getUsers(
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            Pageable pageable) {
        Page<User> response;
        if (!phoneNumber.isEmpty()) {
            response = userService.getUserByPhoneNumber(phoneNumber, 0L, pageable);
        } else {
            response = userService.getUsers(pageable);
        }
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            User response = userService.getUser(id);
            if (response != null) {
                response.setPassword(null);
                return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Không tìm thấy người dùng #: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteUser(@PathVariable Long id) {
            var response = userService.deleteUser(id);
        return ResponseEntity.ok()
                .body(ApiResponse.CREATED(response));
    }

    @PostMapping("/roles")
    private ResponseEntity<?> createNonAdmin(@Valid @RequestBody User user) {
        if (userService.userPhoneNumberExists(user)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Số điện thoại này đã tồn tại"));
        } else if (userService.userEmailExists(user)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Email này đã tồn tại"));
        } else {
            try {
                User response = userService.createNonAdmin(user);
                response.setPassword(null);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.CREATED(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR(e.getMessage()));
            }
        }
    }

    @PostMapping("/employee")
    private ResponseEntity<?> createEmployee(@Valid @RequestBody User user) {
        if (userService.userPhoneNumberExists(user)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Số điện thoại này đã tồn tại"));
        } else if (userService.userEmailExists(user)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Email này đã tồn tại"));
        } else {
            try {
                User response = userService.createEmployee(user);
                response.setPassword(null);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.CREATED(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR(e.getMessage()));
            }
        }
    }

    @GetMapping("countUser")
    public ResponseEntity<?> getCountUser(Pageable pageable) {
        var response = userService.getCountUsersMonth(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/total-users")
    public Long getTotalUsers() {
        return userService.getTotalUsers();
    }

    @GetMapping("/employee")
    public ResponseEntity<?> getAllTables(@RequestParam(value = "branch", required = false) Optional<String> branch,
                                          @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                          Pageable pageable) {
        Page<User> response;
        if (phoneNumber.length() == 10) {
            return ResponseEntity.ok(ApiResponse.SUCCESS(userService.findByPhoneNumberAndBranchId(phoneNumber, Long.parseLong(branch.get()), pageable)));
        }
        if (!phoneNumber.isEmpty()) {
            response = userService.getEmployee(branch, pageable);
        } else {
            response = userService.getUserByPhoneNumber(phoneNumber, Long.parseLong(branch.get()), pageable);
        }
        response.forEach(res -> res.setPassword(null));
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/change-role")
    public ResponseEntity<?> changeRole(@RequestParam(value = "userId", required = false) Integer userId,
                                        @RequestParam(value = "role", required = false) String role,
                                        @RequestParam(value = "branchId", required = false) Integer branchId) {
        return ResponseEntity.ok(ApiResponse.SUCCESS(userService.changeRole(userId, branchId, role)));
    }
}
