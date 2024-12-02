package org.edu.restaurantapi.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationGoogleRequest {
    String email;
    String googleId; // dùng như password
    String name;
    String picture;
}
