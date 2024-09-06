package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Discount code cannot be empty")
    @Size(max = 10, message = "Discount code cannot exceed 10 characters")
    @Column(unique = true)
    String code;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be greater than 0")
    Integer quantity;

    @NotNull(message = "Expiration date must not be null")
    @FutureOrPresent(message = "Expiration date must be in the future or today")
    LocalDate expirationDate;

    LocalDate date = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "discount_method_id")
    DiscountMethod method;

    @NotNull(message = "Quota must not be null")
    @Positive(message = "Quota must be greater than 0")
    Double quota;

    @NotNull(message = "Value must not be null")
    @Positive(message = "Value must be greater than 0")
    Double value;
}
