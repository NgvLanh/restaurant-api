package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tablesReservations")
public class TableReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDate createDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    Boolean isDelete = false;
}
