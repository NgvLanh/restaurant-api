package org.edu.restaurantapi.config;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi._enum.AdminRole;
import org.edu.restaurantapi._enum.Role;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

// Cấu hình chạy lên là tạo role ADMIN và tài khoản ADMIN
// EMAIL: admin@gmail.com
// PASSWORD: admin -- có hash password
@Slf4j
@Configuration
public class ApplicationInitConfig {
    private final String adminName = "ADMIN";
    private final String email = "admin@gmail.com";
    private final String password = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail(email).isEmpty()) {
                User user = User.builder()
                        .fullName(adminName)
                        .phoneNumber("0000000000")
                        .email(email)
                        .password(PasswordUtil.hashPassword(password))
                        .role(Set.of(Role.ADMIN))
                        .isDelete(true)
                        .build();
                userRepository.save(user);
                log.info("ADMIN ACCOUNT ==> EMAIL: admin@gmail.com - PASSWORD: admin");
            }
        };
    }
}
