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
@Entity(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Start time cannot be empty")
    LocalDate startTime;

    @NotNull(message = "End time cannot be empty")
    LocalDate endTime;

    Boolean status = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    Boolean isDelete = false;
}
