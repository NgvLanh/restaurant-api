package org.edu.restaurantapi.request;

import lombok.Data;
import org.edu.restaurantapi._enum.OrderStatus;

@Data
public class OrderRequest {
    Long branchId;
    Long addressId;
    Long discountId;
    OrderStatus orderStatus;
    Double total;
    Long userId;
}
