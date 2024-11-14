package org.edu.restaurantapi.service;

import org.edu.restaurantapi._enum.Role;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;


    public User getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();
        User user = userRepository.findById(Long.parseLong(id)).orElse(null);
        user.setPassword(null);
        return user;
    }

    public User createUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setRoles(Set.of("CLIENT"));
        return userRepository.save(user);
    }

    public User createNonAdmin(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setRoles(Set.of("NON_ADMIN"));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        if (updatedUser.getPassword() != null) {
            updatedUser.setPassword(PasswordUtil.hashPassword(updatedUser.getPassword()));
        }
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFullName(updatedUser.getFullName()
                    != null ? updatedUser.getFullName() : existingUser.getFullName());
            existingUser.setPassword(updatedUser.getPassword()
                    != null ? updatedUser.getPassword() : existingUser.getPassword());
            return userRepository.save(existingUser);
        }).orElse(null);
    }

    public Page<User> getUsers(Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return userRepository.findUserByIsDeleteFalse(pageableSorted);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Page<User> getUserByPhoneNumber(String phoneNumber, Pageable pageable) {
        return userRepository.findByPhoneNumberContaining(phoneNumber, pageable);
    }

    public Boolean deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setIsDelete(true);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    // check email + phone number
    public Boolean userEmailExists(User user) {
        return userRepository.findByEmailAndIsDeleteFalse(user.getEmail()).isPresent();
    }

    public Boolean userPhoneNumberExists(User user) {
        return userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent();
    }

//    public Page<User> getUsersByBranch(String branchId, Pageable pageable) {
//        Long branch = Long.valueOf(branchId);
//        if (branchId.isEmpty()) {
//            return userRepository.findUserByIsDeleteFalse(pageable);
//        }
//        return userRepository.findByBranchId(branch, pageable);
//    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmailAndIsDeleteFalse(email);
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Long getTotalUsers() {
        return userRepository.countTotalRegisteredUsers();
    }
}
