package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.edu.restaurantapi._enum.AdminRole;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự")
    String fullName;

    String image;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại phải là 10 hoặc 11 chữ số")
    @Column(unique = true)
    String phoneNumber;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    @Column(unique = true)
    String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    String password;

    Boolean activated = true;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    Branch branch;

    AdminRole adminRole;

    @JsonIgnore
    Boolean isDelete = false;
}
