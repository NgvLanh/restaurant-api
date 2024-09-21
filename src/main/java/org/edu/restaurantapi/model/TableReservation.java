package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tablesReservations")
public class TableReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDate createAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "table_id")
    Table table;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    Reservation reservation;

    Boolean isDelete = false;
}
