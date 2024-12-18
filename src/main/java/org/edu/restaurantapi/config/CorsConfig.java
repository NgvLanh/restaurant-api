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
            "https://dx9zddr6-4000.asse.devtunnels.ms",
            "http://localhost:4000",
            "https://ac5a-2402-800-63b5-d781-cc57-e27b-b580-7153.ngrok-free.app",
    };

    private final String[] methods = {
            "GET",
            "POST",
            "PUT",
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
