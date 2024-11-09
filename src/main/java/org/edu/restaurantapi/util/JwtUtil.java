package org.edu.restaurantapi.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

public class JwtUtil {

//    @Value("${jwt.signerKey}")
//    private String signerKey;

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .issuer("DLT")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            String signerKey = "NHpqb2tvaTVxYjh3Y3doYTQzMHNkOHUweDg5OWtmcmZ1OXB5cThhZ2huZnhpOHFlYnVwYndpaXkzams3azhydHl4a256dW1iNnhhbWRzdGQxd2RyeWplbXJiaHAzeTJ3a2JrejhlNjJ4YTc3OXp6Z3Nwa2UyMGpnZ3l3cHJ2bGI=";
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user != null && user.getRole() != null && !user.getRole().isEmpty()) {
            stringJoiner.add(user.getRole().toString());
        }
        return stringJoiner.toString();
    }

}
