package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orderStatus")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Tên trạng thái đơn hàng không được để trống")
    @Size(max = 50, message = "Tên trạng thái đơn hàng không được vượt quá 50 ký tự")
    @Column(unique = true)
    String name;

    String colorCode; // Lưu  màu cho đẹp

}
