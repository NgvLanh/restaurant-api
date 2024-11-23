package org.edu.restaurantapi.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@AllArgsConstructor
public class ReserTableOffLineRequest {
    Long branchId;
    String fullName;
    String phoneNumber;

    LocalDate bookingDate;

    LocalTime startTime;

    Long[] tableIds;
}
