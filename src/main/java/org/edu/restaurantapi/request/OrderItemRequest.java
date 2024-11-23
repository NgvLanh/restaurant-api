package org.edu.restaurantapi.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    Long dishId;
    Long orderId;
    Double price;
    Integer quantity;
}
