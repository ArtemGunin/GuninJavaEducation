package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Phone extends Product {
    private final String model;
    private final Manufacturer manufacturer;
    private List<String> details;

    public Phone(String title, int count, double price,
                 String model, Manufacturer manufacturer) {
        super(title, count, price, ProductType.PHONE);
        this.model = model;
        this.manufacturer = manufacturer;
    }

    public Phone(String id, String title, int count, double price,
                 String model, Manufacturer manufacturer) {
        super(id, title, count, price, ProductType.PHONE);
        this.model = model;
        this.manufacturer = manufacturer;
    }

    public Phone(String title, int count, double price,
                 String model, Manufacturer manufacturer, List<String> details) {
        super(title, count, price, ProductType.PHONE);
        this.model = model;
        this.manufacturer = manufacturer;
        this.details = details;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "manufacturer=" + manufacturer +
                ", model=" + model +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + String.format("%.2f", price) +
                '}';
    }
}
