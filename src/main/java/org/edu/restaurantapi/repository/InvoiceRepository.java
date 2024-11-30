package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
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

    @Procedure(name = "GetMonthlyRevenue")
    List<Object[]> getMonthlyRevenue();

    @Query(value = """
            WITH week_dates AS (
                SELECT CURDATE() - INTERVAL (WEEKDAY(CURDATE()) - 0) DAY AS booking_date -- Thứ 2
                UNION ALL SELECT CURDATE() - INTERVAL (WEEKDAY(CURDATE()) - 1) DAY -- Thứ 3
                UNION ALL SELECT CURDATE() - INTERVAL (WEEKDAY(CURDATE()) - 2) DAY -- Thứ 4
                UNION ALL SELECT CURDATE() - INTERVAL (WEEKDAY(CURDATE()) - 3) DAY -- Thứ 5
                UNION ALL SELECT CURDATE() - INTERVAL (WEEKDAY(CURDATE()) - 4) DAY -- Thứ 6
                UNION ALL SELECT CURDATE() - INTERVAL (WEEKDAY(CURDATE()) - 5) DAY -- Thứ 7
                UNION ALL SELECT CURDATE() - INTERVAL (WEEKDAY(CURDATE()) - 6) DAY -- Chủ nhật
            )
            SELECT 
                wd.booking_date,
                COUNT(r.id) AS total_reservations
            FROM 
                week_dates wd
            LEFT JOIN 
                reservations r
            ON 
                wd.booking_date = DATE(r.booking_date)
            GROUP BY 
                wd.booking_date
            ORDER BY 
                wd.booking_date;
            """, nativeQuery = true)
    List<Object[]> getWeeklyReservations();

    @Query(value = "SELECT " +
            "    CONCAT(YEAR(CURDATE()), '-', LPAD(month, 2, '0')) AS month, " +
            "    COALESCE(COUNT(CASE WHEN o.address_id IS NOT NULL AND o.table_id IS NULL THEN 1 END), 0) AS orders_with_address, " +
            "    COALESCE(COUNT(CASE WHEN o.address_id IS NULL AND o.table_id IS NOT NULL THEN 1 END), 0) AS orders_with_table " +
            "FROM " +
            "    (SELECT 1 AS month UNION ALL " +
            "     SELECT 2 UNION ALL " +
            "     SELECT 3 UNION ALL " +
            "     SELECT 4 UNION ALL " +
            "     SELECT 5 UNION ALL " +
            "     SELECT 6 UNION ALL " +
            "     SELECT 7 UNION ALL " +
            "     SELECT 8 UNION ALL " +
            "     SELECT 9 UNION ALL " +
            "     SELECT 10 UNION ALL " +
            "     SELECT 11 UNION ALL " +
            "     SELECT 12) months " +
            "LEFT JOIN orders o ON DATE_FORMAT(o.time, '%Y-%m') = CONCAT(YEAR(CURDATE()), '-', LPAD(month, 2, '0')) " +
            "GROUP BY " +
            "    month " +
            "ORDER BY " +
            "    month", nativeQuery = true)
    List<Object[]> findMonthlyOrderStatistics();
}
