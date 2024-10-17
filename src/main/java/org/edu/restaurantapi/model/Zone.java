package org.edu.restaurantapi.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "zones")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank(message = "Zone address cannot be empty")
    String address;
    @NotBlank(message = "Zone address details cannot be empty")
    String addressDetails;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;

    Boolean isDelete = false;
}
