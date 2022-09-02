package com.service;

import com.context.Autowired;
import com.context.Singleton;
import com.model.Invoice;
import com.model.product.Product;
import com.repository.mongoDB.InvoiceRepositoryDBMongo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Singleton
public class InvoiceServiceDB {

    private final InvoiceRepositoryDBMongo invoiceRepositoryDB;

    private static InvoiceServiceDB instance;

    @Autowired
    private InvoiceServiceDB(InvoiceRepositoryDBMongo invoiceRepositoryDB) {
        this.invoiceRepositoryDB = invoiceRepositoryDB;
    }

    public static InvoiceServiceDB getInstance() {
        if (instance == null) {
            instance = new InvoiceServiceDB(InvoiceRepositoryDBMongo.getInstance());
        }
        return instance;
    }

    public static InvoiceServiceDB getInstance(final InvoiceRepositoryDBMongo repository) {
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
        invoiceRepositoryDB.save(invoice);
    }

    public List<Invoice> getInvoicesWithSumMoreThen(double lowerBound) {
        return invoiceRepositoryDB.getInvoiceListWithSumConditions(lowerBound);
    }

    public long getCountOfInvoices() {
        return invoiceRepositoryDB.getCount();
    }

    public boolean updateTime(String id, LocalDateTime localDateTime) {
        return invoiceRepositoryDB.updateTime(id, localDateTime);
    }

    public Map<Double, List<Invoice>> groupInvoicesBySum() {
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
