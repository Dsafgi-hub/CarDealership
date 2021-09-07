package ru.bachinin.cardealership.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bachinin.cardealership.entities.Invoice;
import ru.bachinin.cardealership.exceptions.EntityNotFoundException;
import ru.bachinin.cardealership.repositories.InvoiceRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoicesController {

    private final InvoiceRepository invoiceRepository;
    private final String className = Invoice.class.getName();

    @Autowired
    public InvoicesController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping()
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Invoice getInvoices(@PathVariable Long id) throws EntityNotFoundException {
        if (invoiceRepository.existsById(id)) {
            return invoiceRepository.getById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @PostMapping()
    public Invoice createInvoice(Invoice invoice) {
        invoice.setCreatedAt(LocalDate.now());
        return invoiceRepository.save(invoice);
    }

    @PutMapping("/{id}")
    public Invoice updateInvoice(@PathVariable Long id,
                                 @RequestBody Invoice invoice) throws EntityNotFoundException {
        if (invoiceRepository.existsById(id)) {
            Invoice oldInvoices = invoiceRepository.getById(id);
            oldInvoices.setUpdatedAt(LocalDate.now());
            oldInvoices.setCreatedBy(invoice.getCreatedBy());
            return invoiceRepository.save(oldInvoices);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Long id) throws EntityNotFoundException {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id, className);
        }
    }
}
