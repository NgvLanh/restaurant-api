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
    @Query("SELECT SUM(i.total) FROM invoices i WHERE i.isDelete = false")
    Double getTotalRevenue();

    @Query("SELECT EXTRACT(YEAR FROM i.date) AS nam, " +
            "EXTRACT(MONTH FROM i.date) AS thang, " +
            "SUM(i.total) AS tong_doanh_thu " +
            "FROM invoices i " +
            "WHERE i.isDelete = false " +
            "GROUP BY EXTRACT(YEAR FROM i.date), EXTRACT(MONTH FROM i.date) " +
            "ORDER BY EXTRACT(YEAR FROM i.date), EXTRACT(MONTH FROM i.date)")
    Page<Map<String, Object>> getMonthlyRevenue(Pageable pageable);
}
