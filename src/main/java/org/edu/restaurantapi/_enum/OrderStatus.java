package org.edu.restaurantapi._enum;

public enum OrderStatus {
    PENDING_CONFIRMATION, // Chờ xác nhận
    CONFIRMED,            // Đã xác nhận
    ORDERED,              // Đã đặt món
    IN_KITCHEN,           // Đang chế biến
    READY_TO_SERVE,       // Sẵn sàng phục vụ
    SERVED,               // Đã phục vụ (dành cho ăn tại bàn)
    DELIVERY,             // Đang giao (dành cho giao hàng)
    DELIVERED,            // Đã giao (dành cho giao hàng)
    CANCELLED,            // Đã hủy
    PAID;                 // Đã thanh toán
}
