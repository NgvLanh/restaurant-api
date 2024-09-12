package org.edu.restaurantapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.signer}")
    private String jwtSecret;

    private final String[] POST_API = {
            "/auth/login",
    };

    private final String[] GET_API = {
            "/api/files/*"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(HttpMethod.POST, POST_API).permitAll()
                            .requestMatchers(HttpMethod.GET , GET_API).permitAll()
                            // khá thủ công
//                            .requestMatchers(HttpMethod.GET, "/api/users").hasRole(Roles.ADMIN.name())
                            .anyRequest().authenticated();
                });
        // TODO: Cấu hình OAuth2 Resource Server để sử dụng JWT cho việc xác thực.
        httpSecurity
                .oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()));
                });

        // TODO: Vô hiệu hóa CSRF (Cross-Site Request Forgery) protection.
        // TODO: Điều này thường cần thiết khi ứng dụng không sử dụng session.
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        // TODO: TODO: Xây dựng và trả về cấu hình SecurityFilterChain.
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecret.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec) // TODO: Cung cấp secret key cho JWT decoder.
                .macAlgorithm(MacAlgorithm.HS512) // TODO: Sử dụng thuật toán HS512 cho chữ ký JWT.
                .build(); // TODO: Xây dựng và trả về JwtDecoder.
    }
}
