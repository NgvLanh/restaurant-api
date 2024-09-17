package org.edu.restaurantapi.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.request.AuthenticationRequest;
import org.edu.restaurantapi.response.AuthenticationResponse;
import org.edu.restaurantapi.response.IntrospectResponse;
import org.edu.restaurantapi.util.JwtUtil;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class AuthenticationService {

    @Value("${jwt.signerKey}")
    private String signerKey;
    
    @Autowired
    private UserRepository userRepository;

    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var user = userRepository.findByEmailOrPhoneNumber(request.getUsername(), request.getUsername())
                .orElse(null);
        if (user != null) {
            var result = PasswordUtil.checkPassword(request.getPassword(), user.getPassword());
            if (result) {
                JwtUtil jwtUtil = new JwtUtil();
                var token = jwtUtil.generateToken(user);
                return AuthenticationResponse.builder().authenticated(true).token(token).build();
            }
        }
        return AuthenticationResponse.builder().authenticated(false).token("").build();
    }

    public IntrospectResponse introspect(String authHeader) throws JOSEException, ParseException {
        // Kiểm tra định dạng của header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }
        // Trích xuất token từ header
        String token = authHeader.substring(7);
        // Chữ ký để kiểm tra JWT
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        // Phân tích JWT
        SignedJWT signedJWT = SignedJWT.parse(token);
        // Kiểm tra chữ ký
        boolean verified = signedJWT.verify(verifier);
        // Lấy thông tin và kiểm tra thời gian hết hạn
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        Date expirationTime = claimsSet.getExpirationTime();
        return IntrospectResponse.builder()
                .valid(verified && expirationTime != null && expirationTime.after(new Date()))
                .build();
    }
}
