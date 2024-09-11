package org.edu.restaurantapi.controller;

import com.nimbusds.jose.JOSEException;
import org.edu.restaurantapi.request.AuthenticationRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    private ResponseEntity<?> authenticated(@RequestBody AuthenticationRequest request) {
        var authenticated = authenticationService.authenticated(request);
        if (authenticated.getAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.SUCCESS(authenticated));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.UNAUTHORIZED("Invalid username or password"));
    }

    @PostMapping("/introspect")
    private ResponseEntity<?> introspect(@RequestHeader("Authorization") String authHeader)
            throws ParseException, JOSEException {
        var introspected = authenticationService.introspect(authHeader);
        if (introspected.getValid()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.SUCCESS(introspected));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.UNAUTHORIZED("Invalid token"));
    }

}
