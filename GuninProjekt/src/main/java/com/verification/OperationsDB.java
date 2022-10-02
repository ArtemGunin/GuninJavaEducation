package com.verification;

import com.model.Invoice;
import com.model.product.Product;
import com.repository.mongoDB.PhoneRepositoryDBMongo;
import com.repository.mongoDB.TVRepositoryDBMongo;
import com.repository.mongoDB.ToasterRepositoryDBMongo;
import com.service.InvoiceServiceDB;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

import java.time.LocalDateTime;
import java.util.*;

public class OperationsDB {

    private static final InvoiceServiceDB INVOICE_SERVICE_DB = InvoiceServiceDB.getInstance();
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();
    private static final PhoneRepositoryDBMongo PHONE_REPOSITORY_DB = PhoneRepositoryDBMongo.getInstance();
    private static final ToasterRepositoryDBMongo TOASTER_REPOSITORY_DB = ToasterRepositoryDBMongo.getInstance();
    private static final TVRepositoryDBMongo TV_REPOSITORY_DB = TVRepositoryDBMongo.getInstance();
    private static final Random RANDOM = new Random();

    public void run() {
        createInvoices();
        System.out.println("Invoices List:\n" + INVOICE_SERVICE_DB.getAllInvoices().toString());
        Invoice invoiceToTimeUpdate = INVOICE_SERVICE_DB.getInvoiceByIndex(1);
        System.out.println("\nTime of invoice before updating: " + invoiceToTimeUpdate.getTime());
        System.out.println("Time updated - " + INVOICE_SERVICE_DB.updateTime(invoiceToTimeUpdate.getId(), LocalDateTime.now()));
        System.out.println("Time of invoice after updating: " + INVOICE_SERVICE_DB.getInvoiceById(invoiceToTimeUpdate.getId()).getTime());
        System.out.println("\n\nInvoices with sum, more then 3500: ");
        INVOICE_SERVICE_DB.getInvoicesWithSumMoreThen(3500.0).forEach(invoice -> System.out.println("Id: " + invoice.getId() + "\nSum: " + invoice.getSum()));
        System.out.println("\nCount of invoices = " + INVOICE_SERVICE_DB.getCountOfInvoices());
        System.out.println("\nInvoice groups by sum: ");
        Map<Double, List<Invoice>> groupingInvoices = INVOICE_SERVICE_DB.groupInvoicesBySum();
        groupingInvoices.keySet().forEach(sum -> {
            System.out.println("Invoice sum - " + sum);
            System.out.println("Invoices: ");
            groupingInvoices.get(sum).forEach(invoice -> System.out.println(invoice.getId()));
        });
    }

    private void createProducts() {
        PHONE_SERVICE.createAndSaveProducts(30);
        TOASTER_SERVICE.createAndSaveProducts(30);
        TV_SERVICE.createAndSaveProducts(30);
    }

    private List<List<Product>> createProductLists() {
        List<List<Product>> productLists = new LinkedList<>();
        for (int i = 1; i < 10; i++) {
            productLists.add(new ArrayList<>());
        }
        List<Product> allProductsList = new ArrayList<>();
        allProductsList.addAll(PHONE_REPOSITORY_DB.getAll());
        allProductsList.addAll(TOASTER_REPOSITORY_DB.getAll());
        allProductsList.addAll(TV_REPOSITORY_DB.getAll());
        allProductsList.forEach(product -> {
            int randomList = RANDOM.nextInt(9);
            productLists.get(randomList).add(product);
        });
        return productLists;
    }

    private void createInvoices() {
        createProducts();
        createProductLists().forEach(INVOICE_SERVICE_DB::createAndSaveInvoice);
    }
}
