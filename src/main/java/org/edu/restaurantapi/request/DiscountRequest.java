package org.edu.restaurantapi.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.edu.restaurantapi._enum.DiscountMethod;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {
    String code;
    Integer quantity;
    LocalDate endDate;
    LocalDate startDate;
    DiscountMethod discountMethod;
    Double quota;
    Double value;
    Long branchId;
}
