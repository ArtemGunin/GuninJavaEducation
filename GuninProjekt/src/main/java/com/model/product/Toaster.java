package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Toaster extends Product {
    private final String model;
    private final int power;
    private final Manufacturer manufacturer;
    private List<String> details;
    private String currency;
    private LocalDateTime created;
    private Body body;

    public Toaster(String title, int count, double price, String model,
                   int power, Manufacturer manufacturer) {
        super(title, count, price, ProductType.TOASTER);
        this.model = model;
        this.power = power;
        this.manufacturer = manufacturer;
    }

    public Toaster(String id, String title, int count, double price,
                   String model, int power, Manufacturer manufacturer) {
        super(id, title, count, price, ProductType.TOASTER);
        this.model = model;
        this.power = power;
        this.manufacturer = manufacturer;
    }

    public Toaster(String title, int count, double price, String model,
                   int power, Manufacturer manufacturer, List<String> details) {
        super(title, count, price, ProductType.TOASTER);
        this.model = model;
        this.power = power;
        this.manufacturer = manufacturer;
        this.details = details;
    }

    public Toaster(String title, int count, double price, String currency,
                   String model, int power, Manufacturer manufacturer,
                   LocalDateTime created, Body body) {
        super(title, count, price, ProductType.TOASTER);
        this.model = model;
        this.power = power;
        this.manufacturer = manufacturer;
        this.currency = currency;
        this.created = created;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Toaster{" +
                "manufacturer=" + manufacturer +
                ", model=" + model +
                ", power=" + power +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + String.format("%.2f", price) +
                '}';
    }
}
