package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    @Size(min = 8, message = "Mã giảm giá phải từ 8 ký tự ký tự.")
    @Size(max = 16, message = "Mã giảm giá không được vượt quá 16 ký tự.")
    String code;

    @NotNull(message = "Số lượng mã giảm giá không được để trống.")
    @Positive(message = "Số lượng mã giảm giá phải lớn hơn 0.")
    Integer quantity;

    @NotNull(message = "Ngày kết thúc không được để trống.")
    @FutureOrPresent(message = "Ngày kết thúc phải là ngày hôm nay hoặc trong tương lai.")
    LocalDate endDate;

    @NotNull(message = "Ngày bắt đầu không được để trống.")
    @FutureOrPresent(message = "Ngày bắt đầu phải là ngày hôm nay hoặc trong tương lai.")
    LocalDate startDate;

    LocalDate createDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "discount_method_id")
    DiscountMethod discountMethod;

    @NotNull(message = "Hạn mức áp dụng không được để trống.")
    @Positive(message = "Hạn mức áp dụng phải lớn hơn 0.")
    Double quota;

    @NotNull(message = "Giá trị giảm giá không được để trống.")
    @Positive(message = "Giá trị giảm giá phải lớn hơn 0.")
    Double value;

    Boolean isDelete = false;

}
