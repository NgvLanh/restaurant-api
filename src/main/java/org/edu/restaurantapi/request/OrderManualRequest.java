package org.edu.restaurantapi.request;

import lombok.Data;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Table;

@Data
public class OrderManualRequest {
    String fullName;
    String phoneNumber;
    Table table;
    Branch branch;
}
