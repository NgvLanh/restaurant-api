package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Role;
import org.edu.restaurantapi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Boolean deleteRole(Long id) {
        return roleRepository.findById(id).map(role -> {
            role.setIsDelete(true);
            roleRepository.save(role);
            return true;
        }).orElse(false);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
