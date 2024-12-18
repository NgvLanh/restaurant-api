package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.edu.restaurantapi._enum.DiscountMethod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Mã giảm giá không được để trống.")
    @Size(min = 1, message = "Mã giảm giá phải từ 0 ký tự.")
    @Size(max = 16, message = "Mã giảm giá không được vượt quá 16 ký tự.")
    String code;

    @NotNull(message = "Số lượng mã giảm giá không được để trống.")
    @Positive(message = "Số lượng mã giảm giá phải lớn hơn 0.")
    Integer quantity;

    @NotNull(message = "Ngày kết thúc không được để trống.")
    LocalDate endDate;

    @NotNull(message = "Ngày bắt đầu không được để trống.")
    LocalDate startDate;

    @Builder.Default
    LocalDate createDate = LocalDate.now();

    DiscountMethod discountMethod;

    @NotNull(message = "Hạn mức áp dụng không được để trống.")
    @Positive(message = "Hạn mức áp dụng phải lớn hơn 0.")
    Double quota;

    @NotNull(message = "Giá trị giảm giá không được để trống.")
    @Positive(message = "Giá trị giảm giá phải lớn hơn 0.")
    Double value;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference
    Branch branch;

    @Builder.Default
    Boolean active = true;

}
