package org.edu.restaurantapi._enum;

public enum OrderStatus {
    // Dùng chung
    PENDING,               // Chờ xác nhận (Chung)
    CONFIRMED,             // Đã xác nhận (Chung)
    ORDERED,               // Đã đặt món (Chung)
    CANCELLED,             // Đã hủy (Chung)
    PAID,                  // Đã thanh toán (Chung)
    ALL,                   // Dùng để select (Không phải trạng thái thực tế)

    // Dành cho ăn tại nhà hàng
    IN_KITCHEN,            // Đang chế biến (Ăn tại nhà hàng)
    READY_TO_SERVE,        // Sẵn sàng phục vụ (Ăn tại nhà hàng)
    SERVED,                // Đã phục vụ (Ăn tại nhà hàng)

    // Dành cho giao hàng
    SHIPPED,               // Đang giao (Giao hàng)
    DELIVERED              // Đã giao (Giao hàng)
}
