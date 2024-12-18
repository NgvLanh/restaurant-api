package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "otp")
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "OTP code cannot be empty")
    @Column(name = "otp_code", nullable = false)
    String otpCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "expires_at")
    LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

    @Builder.Default
    Boolean active = true;
}
