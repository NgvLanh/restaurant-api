package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Category name cannot be empty")
    @Column(unique = true)
    String name;

    @NotBlank(message = "Category description cannot be empty.")
    String description;

    Boolean isDelete = false;
}
