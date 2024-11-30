package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tables")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer number;

    @Min(value = 1, message = "Số ghế tối thiểu phải là 1")
    @Max(value = 20, message = "Số ghế không được vượt quá 20")
    Integer seats;


    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Reservation> reservations;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    Zone zone;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference
    Branch branch;

    @Builder.Default
    Boolean isDelete = false;

}
