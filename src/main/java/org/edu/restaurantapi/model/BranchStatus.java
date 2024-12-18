package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "branchStatus")
public class BranchStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Tình trạng của chi nhán ko dược bỏ trống")
    @Size(max = 50, message = "Tên tình trạng chi nhánh chỉ được dưới 50 ký tự")
    String name;

    String colorCode;

    @Builder.Default
    Boolean active = true;
}
