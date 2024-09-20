package org.edu.restaurantapi.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnPayReturnResponse {
    Integer status;
    Long userId;
    Long orderId;
    Integer amount;
}
