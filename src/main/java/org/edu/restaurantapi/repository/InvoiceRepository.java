package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Map;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT MONTH(i.date) AS month, COUNT(i) AS totalInvoices " +
            "FROM invoices i " +
            "WHERE YEAR(i.date) = YEAR(CURRENT_DATE)  " +
            "GROUP BY MONTH(i.date) " +
            "ORDER BY MONTH(i.date) ASC")
    Page<Map<String, Object>> getInvoiceStatsByMonth(Pageable pageable);

    @Query("SELECT SUM(i.total) FROM invoices i ")
    Double getTotalRevenue();

    @Query("SELECT EXTRACT(YEAR FROM i.date) AS nam, " +
            "EXTRACT(MONTH FROM i.date) AS thang, " +
            "SUM(i.total) AS tong_doanh_thu " +
            "FROM invoices i " +
            "GROUP BY EXTRACT(YEAR FROM i.date), EXTRACT(MONTH FROM i.date) " +
            "ORDER BY EXTRACT(YEAR FROM i.date), EXTRACT(MONTH FROM i.date)")
    Page<Map<String, Object>> getMonthlyRevenue(Pageable pageable);

}
