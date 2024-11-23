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
public class ReservationOnlineRequest {
    Long branchId;
    LocalDate bookingDate;
    String fullName;
    String email;
    String notes;
    String phoneNumber;
    LocalTime startTime;
    Long[] tableIds;
}
