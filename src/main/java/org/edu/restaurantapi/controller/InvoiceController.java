package org.edu.restaurantapi.controller;

import org.edu.restaurantapi.model.Invoice;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<?> getAllInvoices(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.SUCCESS(invoiceService.getAllInvoices(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(ApiResponse.SUCCESS(invoiceService.createInvoice(invoice)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoiceDetails) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, invoiceDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        Boolean response = invoiceService.deleteInvoice(id);
        if (response) {
            return ResponseEntity.ok()
                    .body(ApiResponse.DELETE("Invoice deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Invoice with id " + id + " not found"));
        }
    }






    @GetMapping("/countInvoice")
    public ResponseEntity<?> getInvoice(Pageable pageable) {
        var response = invoiceService.getCountInvoiceMonth(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
