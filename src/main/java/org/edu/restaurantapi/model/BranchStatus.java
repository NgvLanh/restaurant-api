package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.edu.restaurantapi._interface.ExcelExportable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "branchStatus")
public class BranchStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Tình trạng của chi nhán ko dược bỏ trống")
    @Size(max = 50, message = "Tên tình trạng chi nhánh chỉ được dưới 50 ký tự")
    String name;

    String colorCode = "#FFFFFF"; // Lưu  màu cho đẹp
}
