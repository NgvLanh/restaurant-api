package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String phoneNumber;

    String fullName;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    String address;

    String provinceId;

    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    @Size(max = 100, message = "Tỉnh/Thành phố không được vượt quá 100 ký tự")
    String provinceName;

    String districtId;

    @NotBlank(message = "Quận/Huyện không được để trống")
    @Size(max = 100, message = "Quận/Huyện không được vượt quá 100 ký tự")
    String districtName;

    String wardId;

    @NotBlank(message = "Phường/Xã không được để trống")
    @Size(max = 100, message = "Phường/Xã không được vượt quá 100 ký tự")
    String wardName;

    Boolean defaultAddress = false;

    Double fee;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;
}
