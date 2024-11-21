package org.edu.restaurantapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // Có thể bổ sung cấu hình thread pool tại đây nếu cần
}
