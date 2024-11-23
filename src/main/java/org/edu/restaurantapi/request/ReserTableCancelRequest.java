package org.edu.restaurantapi.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@AllArgsConstructor
public class ReserTableCancelRequest {
    Long branchId;
    Long[] reservationIds;
    String reason;
}
