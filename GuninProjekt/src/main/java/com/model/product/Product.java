package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public abstract class Product {
    protected final String id;
    protected String title;
    protected int count;
    protected double price;
    protected final ProductType type;
    private List<String> details;

    protected Product(String title, int count, double price, ProductType type) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.count = count;
        this.price = price;
        this.type = type;
    }

    protected Product(String id, String title, int count, double price, ProductType type) {
        this.id = id;
        this.title = title;
        this.count = count;
        this.price = price;
        this.type = type;
    }

    protected Product(String title, int count, double price, ProductType type, List<String> details) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.count = count;
        this.price = price;
        this.type = type;
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
