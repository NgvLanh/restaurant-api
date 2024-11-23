package org.edu.restaurantapi.request;

import lombok.Data;

@Data
public class TableRequest {
    Long branchId;
    Integer number;
    Integer seats;
    Long zoneId;
}
