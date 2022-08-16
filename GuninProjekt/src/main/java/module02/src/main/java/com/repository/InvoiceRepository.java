package com.repository;

import com.model.Invoice;

import java.util.*;

public class InvoiceRepository {

    private final Map<String, Invoice> invoices;

    private static InvoiceRepository instance;

    private InvoiceRepository() {
        invoices = new HashMap<>();
    }

    public static InvoiceRepository getInstance() {
        if (instance == null) {
            instance = new InvoiceRepository();
        }
        return instance;
    }

    public void save(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Cannot save null invoice!");
        }
        invoices.put(invoice.getCustomer().getId(), invoice);
    }

    public Optional<Invoice> getByUserId(String userId) {
        return Optional.ofNullable(invoices.get(userId));
    }

    public List<Invoice> getAllToList() {
        return new LinkedList<>(invoices.values());
    }
}
