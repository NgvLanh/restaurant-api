package org.edu.restaurantapi.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.edu.restaurantapi.model.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    Boolean authenticated;
    String accessToken;
    User info;
}
