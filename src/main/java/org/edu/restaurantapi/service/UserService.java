package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Role;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.RoleRepository;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String id = context.getAuthentication().getName();
        User user = userRepository.findById(Long.parseLong(id)).orElse(null);
        user.setPassword(null);
        return user;
    }

    public User createUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setRole(roleRepository.findByNameAndIsDeleteFalse("USER").get());
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
            existingUser.setActivated(updatedUser.getActivated()
                    != null ? updatedUser.getActivated() : existingUser.getActivated());
            return userRepository.save(existingUser);
        }).orElse(null);
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findUserByIsDeleteFalse(pageable);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
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
        return userRepository.findByEmail(user.getEmail()).isPresent();
    }

    public Boolean userPhoneNumberExists(User user) {
        return userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent();
    }

    public Page<User> getUsersByBranch(String branchId, Pageable pageable) {
        Long branch = Long.valueOf(branchId);
        if (branchId.isEmpty()) {
            return userRepository.findUserByIsDeleteFalse(pageable);
        }
        return userRepository.findByBranchId(branch, pageable);
    }
}
