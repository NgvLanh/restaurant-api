package org.edu.restaurantapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRequest {

    @NotNull(message = "To can not be empty.")
    String to;

    @NotNull(message = "Subject can not be empty.")
    String subject;

    @NotNull(message = "Text can not be empty.")
    String text;
}
