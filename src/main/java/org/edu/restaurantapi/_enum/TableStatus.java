package org.edu.restaurantapi._enum;

import lombok.Getter;

@Getter
public enum TableStatus {
    AVAILABLE("Bàn còn trống"),
    OCCUPIED("Bàn đang được sử dụng"),
    RESERVED("Bàn đã được đặt trước"),
    OUT_OF_SERVICE("Bàn không sử dụng được");

    private final String description;

    // Constructor
    TableStatus(String description) {
        this.description = description;
    }
}
