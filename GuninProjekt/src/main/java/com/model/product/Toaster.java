package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Toaster extends Product {
    private final String model;
    private final int power;
    private final Manufacturer manufacturer;
    private List<String> details;

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
