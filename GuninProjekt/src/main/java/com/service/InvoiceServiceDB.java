package com.service;

import com.model.Invoice;
import com.model.product.Product;
import com.repository.InvoiceRepositoryDB;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InvoiceServiceDB {

    private final InvoiceRepositoryDB invoiceRepositoryDB;

    public InvoiceServiceDB(InvoiceRepositoryDB invoiceRepositoryDB) {
        this.invoiceRepositoryDB = invoiceRepositoryDB;
    }

    public void createAndSaveInvoice(List<Product> productList) {
        Invoice invoice = new Invoice();
        invoice.setId(UUID.randomUUID().toString());
        invoice.setProducts(productList);
        invoice.setTime(LocalDateTime.now());
        invoice.setSum(productList.stream()
                .mapToDouble(Product::getPrice)
                .sum());
        invoiceRepositoryDB.save(invoice);
    }

    public List<Invoice> getInvoicesWithSumMoreThen(double lowerBound) {
        return invoiceRepositoryDB.getInvoiceListWithSumConditions(lowerBound);
    }

    public int getCountOfInvoices() {
        return invoiceRepositoryDB.getCount();
    }

    public boolean updateTime(String id, LocalDateTime localDateTime) {
        return invoiceRepositoryDB.updateTime(id, localDateTime);
    }

    public Map<Double, Integer> groupInvoicesBySum() {
        return invoiceRepositoryDB.groupInvoices();
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepositoryDB.getAll();
    }

    public Invoice getInvoiceByIndex(int index) {
        return invoiceRepositoryDB.getByIndex(index).orElseThrow();
    }

    public Invoice getInvoiceById(String id) {
        return invoiceRepositoryDB.getById(id).orElseThrow();
    }
}
