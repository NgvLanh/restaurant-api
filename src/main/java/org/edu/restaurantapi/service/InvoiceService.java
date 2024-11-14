package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Invoice;
import org.edu.restaurantapi.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Page<Invoice> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findInvoiceByIsDeleteFalse(pageable);
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
            invoice.setIsDelete(true);
            invoiceRepository.save(invoice);
            return true;
        }).orElse(false);
    }

    public Double getTotalRevenue() {
        return invoiceRepository.getTotalRevenue();
    }

    public Page<Map<String, Object>> getMonthlyRevenue(Pageable pageable) {
        return invoiceRepository.getMonthlyRevenue(pageable);
    }
}
