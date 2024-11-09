package org.edu.restaurantapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.edu.restaurantapi.model.Category;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncDish {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Double price;
    private Integer quantity;
    private Boolean status;
    private Category category;
}
