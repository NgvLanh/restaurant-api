package org.edu.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.edu.restaurantapi._enum.OrderStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Builder.Default
    Date time = new Date();

    OrderStatus orderStatus = OrderStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "table_id")
    @JsonBackReference
    Table table;

    @ManyToOne
    @JoinColumn(name = "address_id")
    Address address;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    Discount discount;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference
    Branch branch;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<OrderItem> orderItems;

    Double total;

    String cancelReason;

    String fullName;

    String phoneNumber;

    @Builder.Default
    Boolean onLineOrder = false;

    @Builder.Default
    Boolean isDelete = false;

    @Builder.Default
    Boolean paymentStatus = false;
}
