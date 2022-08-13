package com.service;

import com.model.Invoice;
import com.model.InvoiceType;
import com.model.product.ProductType;
import com.repository.InvoiceRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnalyticalUtils {

    private final InvoiceRepository repository;

    public AnalyticalUtils(InvoiceRepository repository) {
        this.repository = repository;
    }

    public void printAnalyticalData() {
        printSoldProductsCounts();
        printMinimalCheck();
        printTotalInvoicesAmount();
        InvoiceType invoiceType = InvoiceType.RETAIL;
        printTypeInvoicesCount(invoiceType);
        printCheckIfOneProductType();
        printFirstThreeChecks();
        printCustomersYoungerEighteen();
        printSortInvoices();
    }

    private void printSoldProductsCounts() {
        System.out.println("\nTelephones were sold: " + countSoldProducts(ProductType.TELEPHONE));
        System.out.println("\nTelevision were sold: " + countSoldProducts(ProductType.TELEVISION));
    }

    private long countSoldProducts(ProductType productType) {
        return repository.getAllToList().stream()
                .mapToLong(invoice -> invoice.getProducts().stream()
                        .filter(product -> product.getProductType().equals(productType))
                        .count())
                .sum();
    }

    private void printMinimalCheck() {
        Optional<Long> minCheck = repository.getAllToList().stream()
                .map(Invoice::getTotalPrice)
                .min(Long::compareTo);
        Invoice invoice = repository.getAllToList().stream()
                .filter(invoice1 -> Optional.of(invoice1.getTotalPrice()).equals(minCheck))
                .findAny()
                .orElseThrow();
        System.out.println("\nThe smallest check: " + minCheck.orElseThrow() + "\nCustomer: " + invoice.getCustomer());
    }

    private void printTotalInvoicesAmount() {
        long totalAmount = repository.getAllToList().stream()
                .mapToLong(Invoice::getTotalPrice)
                .sum();
        System.out.println("\nTotal amount of invoices: " + totalAmount);
    }

    private void printTypeInvoicesCount(InvoiceType invoiceType) {
        long invoicesCount = repository.getAllToList().stream()
                .filter(invoice -> invoice.getInvoiceType().equals(invoiceType))
                .count();
        System.out.println("\nCount of " + invoiceType + " checks: " + invoicesCount);
    }

    private void printCheckIfOneProductType() {
        List<Invoice> invoices = new ArrayList<>();
        for (ProductType type : ProductType.values()) {
            invoices.addAll(repository.getAllToList().stream()
                    .filter(invoice -> invoice.getProducts().stream()
                            .allMatch(product -> product.getProductType().equals(type)))
                    .collect(Collectors.toList()));
        }
        System.out.println("\nChecks contain only one type of product: \n" + invoices);
    }

    private void printFirstThreeChecks() {
        System.out.println("\nFirst three checks: \n");
        repository.getAllToList().stream()
                .sorted(Comparator.comparing(Invoice::getCreated))
                .limit(3)
                .forEach(System.out::println);
    }

    private void printCustomersYoungerEighteen() {
        int old = 18;
        System.out.println("\nInvoices of customers younger 18: ");
        repository.getAllToList().stream()
                .filter(invoice -> invoice.getCustomer().getAge() < old)
                .forEach(invoice -> {
                    invoice.setInvoiceType(InvoiceType.LOW_AGE);
                    System.out.println(invoice);
                });
    }

    private int compare(Invoice invoice1, Invoice invoice2) {

        if (invoice1.getCustomer().getAge() > invoice2.getCustomer().getAge()) {
            return -1;
        } else if (invoice1.getCustomer().getAge() < invoice2.getCustomer().getAge()) {
            return 1;
        } else {
            if (invoice1.getProducts().size() < invoice2.getProducts().size()) {
                return -1;
            } else if (invoice1.getProducts().size() > invoice2.getProducts().size()) {
                return 1;
            } else {
                return Long.compare(invoice1.getTotalPrice(), invoice2.getTotalPrice());
            }
        }
    }

    private void printSortInvoices() {
        System.out.println("\nSorted invoices: ");
        repository.getAllToList().stream()
                .sorted(this::compare)
                .forEach(System.out::println);
    }
}
