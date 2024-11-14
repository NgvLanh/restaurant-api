package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Dish;
import org.edu.restaurantapi.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findInvoiceByIsDeleteFalse(Pageable pageable);











    @Query("SELECT MONTH(i.date) AS month, COUNT(i) AS totalInvoices " +
            "FROM invoices i " +
            "WHERE YEAR(i.date) = YEAR(CURRENT_DATE) AND i.isDelete = false " +
            "GROUP BY MONTH(i.date) " +
            "ORDER BY MONTH(i.date) ASC")
    Page<Map<String, Object>> getInvoiceStatsByMonth(Pageable pageable);
}
