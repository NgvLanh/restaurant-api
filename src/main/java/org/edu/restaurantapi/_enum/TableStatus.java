package org.edu.restaurantapi._enum;

public enum TableStatus {
    AVAILABLE,  // Bàn còn trống
    OCCUPIED,   // Bàn đang được sử dụng
    RESERVED,   // Bàn đã được đặt trước
    OUT_OF_SERVICE // Bàn không sử dụng được (bảo trì, hỏng hóc, v.v.)
}
