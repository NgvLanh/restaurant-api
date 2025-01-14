package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    public User getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();
        User user = userRepository.findById(Long.parseLong(id)).orElse(null);
        user.setPassword(null);
        if (user.getImage() != null && !user.getImage().startsWith("http")) {
            user.setImage("http://localhost:8080/api/files/" + user.getImage());
        }
        return user;
    }

    public User createUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setRoles(Set.of("CLIENT"));
        User userCreated = userRepository.save(user);
        Cart cart = Cart.builder().user(userCreated).build();
        cartService.createCart(cart);
        return userCreated;
    }

    public User createNonAdmin(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setRoles(Set.of("NON_ADMIN"));
        return userRepository.save(user);
    }

    public User createEmployee(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setRoles(Set.of("EMPLOYEE"));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        if (updatedUser.getPassword() != null) {
            updatedUser.setPassword(PasswordUtil.hashPassword(updatedUser.getPassword()));
        }
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFullName(updatedUser.getFullName()
                    != null ? updatedUser.getFullName() : existingUser.getFullName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber()
                    != null ? updatedUser.getPhoneNumber() : existingUser.getPhoneNumber());
            existingUser.setEmail(updatedUser.getEmail()
                    != null ? updatedUser.getEmail() : existingUser.getEmail());
            existingUser.setImage(updatedUser.getImage()
                    != null ? updatedUser.getImage() : existingUser.getImage());
            existingUser.setBranch(updatedUser.getBranch() != null
                    ? updatedUser.getBranch() : existingUser.getBranch());
            return userRepository.save(existingUser);
        }).orElse(null);
    }

    public Page<User> getUsers(Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return userRepository.findUsersByActiveTrue(pageableSorted);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Page<User> getUserByPhoneNumber(String phoneNumber, Long branchId, Pageable pageable) {
        return userRepository.findByPhoneNumberContainingAndBranchIdAndActiveTrue(phoneNumber, branchId, pageable);
    }

    public Page<User> findByPhoneNumberAndBranchId(String phoneNumber, Long branchId, Pageable pageable) {
        return userRepository.findByPhoneNumberAndBranchIdAndActiveTrue(phoneNumber, branchId, pageable);
    }

    public Page<User> getEmployee(Optional<String> branch, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return userRepository.findUsersByBranchAndRole(Long.parseLong(branch.get()), pageableSorted);
    }

    public User deleteUser(Long id) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setActive(false);
            return userRepository.save(existingUser);
        }).orElse(null);
    }

    // check email + phone number
    public Boolean userEmailExists(User user) {
        return userRepository.findByEmailAndActiveTrue(user.getEmail()).isPresent();
    }

    public Boolean userPhoneNumberExists(User user) {
        return userRepository.findByPhoneNumberAndActiveTrue(user.getPhoneNumber()).isPresent();
    }

//    public Page<User> getUsersByBranch(String branchId, Pageable pageable) {
//        Long branch = Long.valueOf(branchId);
//        if (branchId.isEmpty()) {
//            return userRepository.findUserByIsDeleteFalse(pageable);
//        }
//        return userRepository.findByBranchId(branch, pageable);
//    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmailAndActiveTrue(email);
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }


    public Page<Map<String, Object>> getCountUsersMonth(Pageable pageable) {
        return userRepository.getUserStatsByMonth(pageable);
    }

    public Long getTotalUsers() {
        String roleName = "CLIENT";
        return userRepository.countTotalRegisteredUsers(roleName);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndActiveTrue(email).orElse(null);
    }

    public String changeRole(Integer userId, Integer branchId, String role) {
        if (role.equals("EMPLOYEE")) {
            userRepository.updateRole(userId, branchId, role);
        } else if (role.equals("NON_ADMIN")) {
            userRepository.updateRole(userId, branchId, role);
        }
        return "OK";
    }
}










