package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
