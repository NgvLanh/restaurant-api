package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank(message = "Role name cannot be empty")
    String name;
    @NotBlank(message = "Role permissions cannot be empty")
    String permissions;

    @JsonIgnore
    Boolean isDelete = false;
}
