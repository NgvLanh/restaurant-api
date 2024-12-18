package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //    @NotNull(message = "Thời gian bắt đầu không được để trống")
    LocalTime startTime;

    LocalTime endTime;

    //    @NotNull(message = "Ngày đặt không được để trống")
    LocalDate bookingDate;

    @Builder.Default
    Date createDate = new Date();

    String fullName;
    String email;
    String phoneNumber;
    String notes;

    @ManyToOne
    @JoinColumn(name = "table_id")
    @JsonBackReference
    Table table;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference // mở comment lúc làm đặt bàn
    Branch branch;

    @OneToOne
    Order order;

    String cancelReason;

    @Builder.Default
    Boolean active = true;
}
