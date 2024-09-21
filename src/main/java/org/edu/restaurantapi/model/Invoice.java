package org.edu.restaurantapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDate date = LocalDate.now();

    @NotNull(message = "Total cannot be empty")
    @Positive(message = "Total must be greater than 0")
    Double total;

    @ManyToOne
    @JoinColumn(name = "invoice_status_id")
    InvoiceStatus invoiceStatus;

    @OneToOne
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment payment;

    Boolean isDelete = false;
}
