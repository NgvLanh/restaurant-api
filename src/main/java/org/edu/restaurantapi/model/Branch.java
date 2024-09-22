package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Branch name cannot be empty")
    @Size(max = 100, message = "Branch name cannot exceed 100 characters")
    String name;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must be 10 or 11 digits")
    @Column(unique = true)
    String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    String address;

    @NotBlank(message = "District cannot be empty")
    @Size(max = 100, message = "District cannot exceed 100 characters")
    String district;

    @NotBlank(message = "City cannot be empty")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    String city;

    @ManyToOne
    @JoinColumn(name = "branch_status_id")
    BranchStatus branchStatus;

    Boolean isDelete = false;
}
