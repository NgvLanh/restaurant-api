package org.edu.restaurantapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.List;

@Configuration
public class CorsConfig {

    private final String[] origins = {
            "http://localhost:3000",
            "http://localhost:5000",
    };

    private final String[] methods = {
            "GET",
            "POST",
            "PATCH",
            "DELETE",
    };

    private final String[] headers = {
            "Authorization",
            "Content-Type"
    };

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // Cho phép gửi cookies và thông tin xác thực
        config.setAllowedOrigins(List.of(origins));  // Cung cấp các origin được phép
        config.setAllowedMethods(List.of(methods));  // Cho phép các phương thức HTTP
        config.setAllowedHeaders(List.of(headers));  // Cho phép các header cụ thể
        source.registerCorsConfiguration("/**", config);  // Áp dụng cấu hình cho tất cả các endpoint
        return new CorsFilter(source);
    }
}
