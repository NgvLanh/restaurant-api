package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "discountMethods")
@Data
public class DiscountMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Discount method cannot be empty")
    @Size(max = 50, message = "Discount method cannot exceed 50 characters")
    @Column(unique = true)
    String name;

    Boolean isDelete = false;
}
