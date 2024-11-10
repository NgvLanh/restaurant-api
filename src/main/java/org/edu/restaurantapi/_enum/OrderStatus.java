package org.edu.restaurantapi._enum;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERED("Đã đặt món", "#FFA500"),          // Màu cam cho "Đã đặt món"
    IN_KITCHEN("Đang chế biến", "#FFFF00"),     // Màu vàng cho "Đang chế biến"
    READY_TO_SERVE("Sẵn sàng phục vụ", "#32CD32"), // Màu xanh lá cho "Sẵn sàng phục vụ"
    SERVED("Đã phục vụ", "#0000FF"),           // Màu xanh dương cho "Đã phục vụ"
    DELIVERY("Đang giao", "#FFD700"),          // Màu vàng gold cho "Đang giao"
    DELIVERED("Đã giao", "#228B22"),           // Màu xanh đậm cho "Đã giao"
    PAID("Đã thanh toán", "#008000"),          // Màu xanh lá cho "Đã thanh toán"
    PENDING("Chờ thanh toán", "#FF6347"),      // Màu đỏ cho "Chờ thanh toán"
    CANCELLED("Đã hủy", "#DC143C");            // Màu đỏ đậm cho "Đã hủy"

    // Constructor
    OrderStatus(String description, String color) {
    }
}
