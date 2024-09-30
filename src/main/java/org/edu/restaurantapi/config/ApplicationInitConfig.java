package org.edu.restaurantapi.config;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Role;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.RoleRepository;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Cấu hình chạy lên là tạo role ADMIN và tài khoản ADMIN
// EMAIL: admin@gmail.com
// PASSWORD: admin -- có hash password
@Slf4j
@Configuration
public class ApplicationInitConfig {
    private final String roleName = "ADMIN";
    private final String email = "admin@gmail.com";
    private final String password = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository) {
        return args -> {
            Role role;
            if (roleRepository.findByNameAndIsDeleteFalse(roleName).isEmpty()) {
                role = roleRepository.save(Role.builder()
                        .name(roleName)
                        .permissions("ALL")
                        .build());
            } else {
                role = roleRepository.findByNameAndIsDeleteFalse(roleName).get();
            }
            if (userRepository.findByEmail(email).isEmpty()) {
                User user = User.builder()
                        .fullName("ADMIN")
                        .phoneNumber("0123456789")
                        .email(email)
                        .password(PasswordUtil.hashPassword(password))
                        .activated(true)
                        .role(role)
                        .build();
                userRepository.save(user);
                log.info("ADMIN ACCOUNT ==> EMAIL: admin@gmail.com - PASSWORD: admin");
            }
        };
    }
}
