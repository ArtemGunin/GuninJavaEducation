package com.service;

import com.context.Autowired;
import com.context.Singleton;
import com.model.Invoice;
import com.model.product.Product;
import com.repository.hibernate.InvoiceRepositoryDBHibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Singleton
public class InvoiceServiceDB {

    private final InvoiceRepositoryDBHibernate invoiceRepositoryDBHibernate;

    private static InvoiceServiceDB instance;

    @Autowired
    private InvoiceServiceDB(InvoiceRepositoryDBHibernate invoiceRepositoryDBHibernate) {
        this.invoiceRepositoryDBHibernate = invoiceRepositoryDBHibernate;
    }

    public static InvoiceServiceDB getInstance() {
        if (instance == null) {
            instance = new InvoiceServiceDB(InvoiceRepositoryDBHibernate.getInstance());
        }
        return instance;
    }

    public static InvoiceServiceDB getInstance(final InvoiceRepositoryDBHibernate repository) {
        if (instance == null) {
            instance = new InvoiceServiceDB(repository);
        }
        return instance;
    }

    public void createAndSaveInvoice(List<Product> productList) {
        Invoice invoice = new Invoice();
        invoice.setTime(LocalDateTime.now());
        invoice.setSum(productList.stream()
                .mapToDouble(Product::getPrice)
                .sum());
        productList.forEach(product -> product
                .setInvoice(invoice));
        invoice.setProducts(productList);
        invoiceRepositoryDBHibernate.save(invoice);
    }

    public List<Invoice> getInvoicesWithSumMoreThen(double lowerBound) {
        return invoiceRepositoryDBHibernate.getInvoiceListWithSumConditions(lowerBound);
    }

    public long getCountOfInvoices() {
        return invoiceRepositoryDBHibernate.getCount();
    }

    public boolean updateTime(String id, LocalDateTime localDateTime) {
        return invoiceRepositoryDBHibernate.updateTime(id, localDateTime);
    }

    public Map<Double, List<Invoice>> groupInvoicesBySum() {
        return invoiceRepositoryDBHibernate.groupInvoices();
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepositoryDBHibernate.getAll();
    }

    public Invoice getInvoiceByIndex(int index) {
        return invoiceRepositoryDBHibernate.getByIndex(index).orElseThrow();
    }

    public Invoice getInvoiceById(String id) {
        return invoiceRepositoryDBHibernate.getById(id).orElseThrow();
    }
}
