package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tables")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Number cannot be null")
    Integer number;

    @Min(value = 1, message = "Seats must be at least 1")
    @Max(value = 20, message = "Seats cannot exceed 20")
    Integer seats;

    @ManyToOne
    @JoinColumn(name = "table_status_id")
    TableStatus tableStatus;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    Zone zone;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;

    Boolean isDelete = false;
}
