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
        String sql = "SELECT * FROM \"public\".\"Invoice\" WHERE sum > ?";
        return invoiceRepositoryDB.getInvoiceListWithSumConditions(sql, lowerBound);
    }

    public int getCountOfInvoices() {
        String parameter = "total";
        String sql = "SELECT COUNT(id) AS " + parameter + " FROM \"public\".\"Invoice\"";
        return invoiceRepositoryDB.getCountBy(sql, parameter);
    }

    public boolean updateTime(String id, LocalDateTime localDateTime) {
        return invoiceRepositoryDB.updateTime(id, localDateTime);
    }

    public Map<Double, Integer> groupInvoicesBySum() {
        String numericColumn = "sum";
        String function = "COUNT";
        String sql = "SELECT " + function + "(id) AS " + function + ", " + numericColumn +
                " FROM \"public\".\"Invoice\" " +
                "GROUP BY " + numericColumn;
        return invoiceRepositoryDB.groupInvoicesBy(sql, numericColumn, function);
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
