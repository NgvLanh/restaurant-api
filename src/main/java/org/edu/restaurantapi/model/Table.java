package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.edu.restaurantapi._enum.TableStatus;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tables")
public class    Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer number;

    @Min(value = 1, message = "Số ghế tối thiểu phải là 1")
    @Max(value = 20, message = "Số ghế không được vượt quá 20")
    Integer seats;

    @Enumerated(EnumType.STRING)
    TableStatus tableStatus = TableStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    Zone zone;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;

    Boolean isDelete = false;
}
