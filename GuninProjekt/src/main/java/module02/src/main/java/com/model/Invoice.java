package com.model;

import com.model.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Invoice {
    private List<Product> products;
    private Customer customer;
    private InvoiceType invoiceType;
    private LocalDateTime created;

    public Invoice(List<Product> products, Customer customer, InvoiceType invoiceType, LocalDateTime created) {
        this.products = products;
        this.customer = customer;
        this.invoiceType = invoiceType;
        this.created = created;
    }

    public long getTotalPrice() {
        return products.stream()
                .mapToLong(Product::getPrice)
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder printProducts = new StringBuilder();
        for (Product product : this.products) {
            printProducts.append(product).append("\n");
        }
        return "\n\nInvoice{" +
                "products:\n" + printProducts +
                "customer=" + customer +
                ", invoiceType=" + invoiceType +
                ", created=" + created +
                "}";
    }
}
