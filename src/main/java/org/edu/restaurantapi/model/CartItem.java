package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cartItems")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Boolean status = true;

    @NotNull(message = "Quantity cannot be empty")
    @Positive(message = "Quantity must be greater than 0")
    Integer quantity;

    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be greater than 0")
    Double price;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    Dish dish;

    Boolean isDelete = false;
}
