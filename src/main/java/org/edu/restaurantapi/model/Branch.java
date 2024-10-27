package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @NotBlank(message = "Tên chi nhánh không được để trống")
    @Size(max = 100, message = "Tên chi nhánh không được vượt quá 100 ký tự")
    String name;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại phải có 10 hoặc 11 chữ số")
    String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    String address;

    @NotBlank(message = "Quận/Huyện không được để trống")
    @Size(max = 100, message = "Quận/Huyện không được vượt quá 100 ký tự")
    String district;

    @NotBlank(message = "Thành phố không được để trống")
    @Size(max = 100, message = "Thành phố không được vượt quá 100 ký tự")
    String city;

    @ManyToOne
    @JoinColumn(name = "branch_status_id")
    BranchStatus branchStatus;

    @JsonIgnore
    Boolean isDelete = false;
}
