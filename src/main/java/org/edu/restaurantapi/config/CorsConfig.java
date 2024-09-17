package org.edu.restaurantapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] origins = {
            "http://localhost:3000",
    };

    private final String[] methods = {
            "GET",
            "POST",
            "PATCH",
            "DELETE",
    };

    private final String[] headers = {
            "Authorization"
    };

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods(methods)
                .allowedHeaders(headers)
                .allowCredentials(true);
    }
}
