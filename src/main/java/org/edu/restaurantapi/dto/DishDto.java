package org.edu.restaurantapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.edu.restaurantapi.model.Category;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;
import java.io.Serializable;

@Data
public class DishDto implements Serializable {

    Long id;

    @NotBlank(message = "Tên món ăn không được để trống")
    String name;

    @NotNull(message = "Hình ảnh món ăn không được để trống")
    MultipartFile file;

    String imageUrl;

    @NotNull(message = "Giá món ăn không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn 0")
    Double price;

    @NotBlank(message = "Mô tả món ăn không được để trống")
    String description;

    Boolean status = true;

    Category category;

    Boolean isDelete = false;
}
