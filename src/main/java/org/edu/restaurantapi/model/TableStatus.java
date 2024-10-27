package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tableStatus")
public class TableStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Tên trạng thái bàn không được để trống")
    @Size(max = 50, message = "Tên trạng thái bàn không được vượt quá 50 ký tự")
    String name;

    String colorCode; // Lưu  màu cho đẹp

}
