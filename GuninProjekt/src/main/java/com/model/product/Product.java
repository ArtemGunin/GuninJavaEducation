package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class Product {
    protected final String id;
    protected String title;
    protected int count;
    protected double price;
    protected final ProductType type;

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

}
