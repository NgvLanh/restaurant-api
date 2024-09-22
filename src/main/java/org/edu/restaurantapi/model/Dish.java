package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Dish name cannot be empty")
    String name;

    @NotBlank(message = "Dish image cannot be empty")
    String image;

    @NotNull(message = "Dish price cannot be empty")
    @Min(value = 0, message = "Price must be greater than zero")
    Double price;

    @NotBlank(message = "Dish description cannot be empty")
    String description;

    Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;

    @JsonIgnore
    Boolean isDelete = false;
}
