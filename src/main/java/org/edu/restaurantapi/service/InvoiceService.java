package org.edu.restaurantapi.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.Data;
import org.edu.restaurantapi.model.Invoice;
import org.edu.restaurantapi.repository.InvoiceRepository;
import org.edu.restaurantapi.response.RevenueResponseByMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private EntityManager entityManager;

    public Page<Invoice> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable);
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Invoice not found"));
        invoice.setTotal(invoiceDetails.getTotal());
        invoice.setOrder(invoiceDetails.getOrder());
        return invoiceRepository.save(invoice);
    }

    public Boolean deleteInvoice(Long id) {
        return invoiceRepository.findById(id).map(invoice -> {
            invoiceRepository.save(invoice);
            return true;
        }).orElse(false);
    }

    public Page<Map<String, Object>> getCountInvoiceMonth(Pageable pageable) {
        return invoiceRepository.getInvoiceStatsByMonth(pageable);
    }

    public Double getTotalRevenue() {
        return invoiceRepository.getTotalRevenue();
    }

    public List<RevenueResponseByMonth> getMonthlyRevenue() {
        Query query = entityManager.createNativeQuery("CALL GetMonthlyRevenue()");
        List<RevenueResponseByMonth> list = new ArrayList<>();
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            var year = result[0];
            var month = result[1];
            var totalRevenue = result[2];
            list.add(RevenueResponseByMonth.builder()
                    .year((Date) year)
                    .month((Long) month)
                    .totalRevenue((Double) totalRevenue)
                    .build());
        }
        return list;
    }

    public List<Object[]> getReversionByWeek(Long branchId) {
        return invoiceRepository.getWeeklyReservations(branchId);
    }

    public List<Object[]> getMonthlyOrderStatistics() {
        return invoiceRepository.findMonthlyOrderStatistics();
    }

    public List<Object[]> getDailyOrderStatistics() {
        return invoiceRepository.findDailyOrderStatistics();
    }
}
