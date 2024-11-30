package org.edu.restaurantapi.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class RevenueResponseByMonth {
    Date year;
    Long month;
    Double totalRevenue;
}
