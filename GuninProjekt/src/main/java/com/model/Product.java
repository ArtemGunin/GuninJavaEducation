package com.model;

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

    protected Product(String title, int count, double price) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.count = count;
        this.price = price;
    }

    protected Product(String id, String title, int count, double price) {
        this.id = id;
        this.title = title;
        this.count = count;
        this.price = price;
    }
}
