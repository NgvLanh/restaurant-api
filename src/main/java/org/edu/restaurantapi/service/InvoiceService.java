package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Invoice;
import org.edu.restaurantapi.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
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

    public Invoice deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null || invoice.getIsDelete()) {
            return null;
        }
        invoice.setIsDelete(true);
        invoiceRepository.save(invoice);
        return invoice;
    }
}
