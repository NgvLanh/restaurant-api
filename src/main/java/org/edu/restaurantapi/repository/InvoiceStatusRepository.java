package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Invoice;
import org.edu.restaurantapi.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatus, Long> {
}
