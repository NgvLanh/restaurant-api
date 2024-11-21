package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @NotBlank(message = "Địa chỉ khu vực không được để trống")
    String name;

    @NotBlank(message = "Chi tiết địa chỉ khu vực không được để trống")
    String address;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference
    Branch branch;


}

