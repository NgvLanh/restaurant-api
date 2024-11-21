package org.edu.restaurantapi._enum;

public enum OrderStatus {
    // Dùng chung
    CANCELLED,             // Đã hủy (Chung)
    PAID,                  // Đã thanh toán (Chung)
    ALL,                   // Dùng để select (Không phải trạng thái thực tế)

    // Dành cho ăn tại nhà hàng
    READY_TO_SERVE,        // Sẵn sàng phục vụ (Ăn tại nhà hàng)
    SERVED,                // Đã phục vụ (Ăn tại nhà hàng)

    // Dành cho giao hàng
    PENDING,               // Chờ xác nhận ()D
    CONFIRMED,             // Đã xác nhận ()
    SHIPPED,               // Đang giao (Giao hàng)
    DELIVERED              // Đã giao (Giao hàng)
}
