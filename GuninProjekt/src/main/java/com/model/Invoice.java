package com.model;

import com.model.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Invoice {
    private String id;
    private double sum;
    private List<Product> products;
    private LocalDateTime time;

    @Override
    public String toString() {
        return "\nInvoice{" +
                "id='" + id + '\'' +
                ", sum=" + sum +
                ", products=" + products +
                ", time=" + time +
                '}';
    }
}
