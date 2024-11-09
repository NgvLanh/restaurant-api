package org.edu.restaurantapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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

    @Value("${jwt.signerKey}")
    private String singerKey;

    private final String[] POST_API = {
            "/api/auth/login",
            "/api/users",
            "/api/email/send-otp",
            "/api/carts/async/*",
    };

    private final String[] GET_API = {
            "/api/files/*",
            "/api/*",
            "/api/dishes/*",
    };

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // Cấu hình bảo mật cho ứng dụng
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Định nghĩa các URL nào yêu cầu xác thực và URL nào thì không
        httpSecurity
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(HttpMethod.POST, POST_API).permitAll() // Các API POST không cần xác thực
                            .requestMatchers(HttpMethod.GET, GET_API).permitAll()  // Các API GET không cần xác thực
                            .anyRequest().authenticated();  // Các yêu cầu khác phải xác thực
                })
                // Cấu hình CORS nếu cần
                .cors(Customizer.withDefaults()) // Bật cấu hình CORS mặc định (phải có cấu hình `CorsConfig` nếu cần)
                // Cấu hình sử dụng OAuth2 Resource Server và JWT để xác thực
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())  // Sử dụng decoder cho JWT
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())) // Sử dụng converter để lấy roles từ JWT
                )
                // Cấu hình xử lý lỗi xác thực bằng CustomAuthenticationEntryPoint
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                // Vô hiệu hóa CSRF protection vì ứng dụng không sử dụng session
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();  // Xây dựng SecurityFilterChain
    }

    // Bean để chuyển đổi token JWT thành quyền (authorities) cho người dùng
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Đặt tiền tố "ROLE_" cho các quyền
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter); // Sử dụng converter để lấy các quyền từ token
        return jwtAuthenticationConverter;
    }

    // Bean tạo decoder để giải mã và xác thực token JWT
    @Bean
    JwtDecoder jwtDecoder() {
        // Tạo SecretKeySpec từ chuỗi secret key để giải mã JWT sử dụng thuật toán HS512
        SecretKeySpec secretKeySpec = new SecretKeySpec(singerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec) // Cung cấp secret key
                .macAlgorithm(MacAlgorithm.HS512) // Sử dụng thuật toán HS512 để xác minh chữ ký
                .build(); // Xây dựng và trả về JwtDecoder
    }
}
