package org.edu.restaurantapi.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnPayRequest {

    @NotNull(message = "Order id can not be empty.")
    Long orderId;

    @NotNull(message = "User id can not be empty.")
    Long userId;

    @NotNull(message = "Amount can not be empty.")
    Integer amount;
}