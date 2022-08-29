package com.verification;

import com.model.Invoice;
import com.model.product.Phone;
import com.model.product.Product;
import com.model.product.TV;
import com.model.product.Toaster;
import com.repository.InvoiceRepositoryDB;
import com.repository.PhoneRepositoryDB;
import com.repository.TVRepositoryDB;
import com.repository.ToasterRepositoryDB;
import com.service.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class OperationsJDBC {

    private static final InvoiceServiceDB INVOICE_SERVICE_DB = new InvoiceServiceDB(new InvoiceRepositoryDB());
    private static final ProductService<Phone> PHONE_SERVICE = PhoneService.getInstance();
    private static final ProductService<Toaster> TOASTER_SERVICE = ToasterService.getInstance();
    private static final ProductService<TV> TV_SERVICE = TVService.getInstance();
    private static final PhoneRepositoryDB PHONE_REPOSITORY_DB = PhoneRepositoryDB.getInstance();
    private static final ToasterRepositoryDB TOASTER_REPOSITORY_DB = ToasterRepositoryDB.getInstance();
    private static final TVRepositoryDB TV_REPOSITORY_DB = TVRepositoryDB.getInstance();
    private static final Random RANDOM = new Random();

    public void run() {
        createInvoices();
        System.out.println("Invoices List:\n" + INVOICE_SERVICE_DB.getAllInvoices().toString());
        System.out.println("\n\nInvoices with sum, more then 3500: ");
        INVOICE_SERVICE_DB.getInvoicesWithSumMoreThen(3500.0)
                .forEach(invoice -> System.out.println("Id: " + invoice.getId() + "\nSum: " + invoice.getSum()));
        System.out.println("\nCount of invoices = " + INVOICE_SERVICE_DB.getCountOfInvoices());
        Invoice invoiceToTimeUpdate = INVOICE_SERVICE_DB.getInvoiceByIndex(1);
        System.out.println("\nTime of invoice before updating: " + invoiceToTimeUpdate.getTime());
        System.out.println("Time updated - "
                + INVOICE_SERVICE_DB.updateTime(invoiceToTimeUpdate.getId(), LocalDateTime.now()));
        System.out.println("Time of invoice after updating: "
                + INVOICE_SERVICE_DB.getInvoiceById(invoiceToTimeUpdate.getId()).getTime());
        System.out.println("\nInvoice groups by sum: " + INVOICE_SERVICE_DB.groupInvoicesBySum());
    }

    private void createProducts() {
        PHONE_SERVICE.createAndSaveProducts(10);
        TOASTER_SERVICE.createAndSaveProducts(10);
        TV_SERVICE.createAndSaveProducts(10);
    }

    private List<Product> createProductList() {
        List<Product> productList = new LinkedList<>();
        int i = 5 + RANDOM.nextInt(5);
        while (i > 0) {
            int a = RANDOM.nextInt(2);
            switch (a) {
                case 0 -> productList.add(PHONE_REPOSITORY_DB.getByIndex(1 + RANDOM.nextInt(9)).orElseThrow());
                case 1 -> productList.add(TOASTER_REPOSITORY_DB.getByIndex(1 + RANDOM.nextInt(9)).orElseThrow());
                case 2 -> productList.add(TV_REPOSITORY_DB.getByIndex(1 + RANDOM.nextInt(9)).orElseThrow());
            }
            i--;
        }
        return productList;
    }

    private void createInvoices() {
        createProducts();
        int j = 10;
        while (j > 0) {
            INVOICE_SERVICE_DB.createAndSaveInvoice(createProductList());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j--;
        }
    }
}
