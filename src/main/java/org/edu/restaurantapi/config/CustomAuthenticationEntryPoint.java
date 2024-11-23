package org.edu.restaurantapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        // Customize the error message
        String errorMessage = "Please log in to continue.";

        // Set HTTP status code
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Set content type for the response
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Create a custom response body
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("status", false);
        errorResponse.put("message", errorMessage);

        // Write the error response as a JSON object
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
