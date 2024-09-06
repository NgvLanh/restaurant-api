package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "branchStatus")
public class BranchStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @NotBlank(message = "Branch status name cannot be empty")
    @Size(max = 50, message = "Branch status name cannot exceed 50 characters")
    @Column(unique = true)
    String name;
}
