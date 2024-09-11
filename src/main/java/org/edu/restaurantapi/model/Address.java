package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    String address;

    @NotBlank(message = "City cannot be empty")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    String city;

    @NotBlank(message = "District cannot be empty")
    @Size(max = 100, message = "District cannot exceed 100 characters")
    String district;

    @NotBlank(message = "Ward cannot be empty")
    @Size(max = 100, message = "Ward cannot exceed 100 characters")
    String ward;

    Boolean status = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
