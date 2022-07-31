package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TV extends Product {
    private final String model;
    private final Manufacturer manufacturer;
    private final int diagonal;
    private List<String> details;

    public TV(String title, int count, double price, String model,
              Manufacturer manufacturer, int diagonal) {
        super(title, count, price, ProductType.TV);
        this.model = model;
        this.manufacturer = manufacturer;
        this.diagonal = diagonal;
    }

    public TV(String id, String title, int count, double price,
              String model, Manufacturer manufacturer, int diagonal) {
        super(id, title, count, price, ProductType.TV);
        this.model = model;
        this.manufacturer = manufacturer;
        this.diagonal = diagonal;
    }

    public TV(String title, int count, double price, String model,
              Manufacturer manufacturer, int diagonal, List<String> details) {
        super(title, count, price, ProductType.TV);
        this.model = model;
        this.manufacturer = manufacturer;
        this.diagonal = diagonal;
        this.details = details;
    }

    @Override
    public String toString() {
        return "TV{" +
                "manufacturer=" + manufacturer +
                ", model=" + model +
                ", diagonal=" + diagonal +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + String.format("%.2f", price) +
                '}';
    }
}
