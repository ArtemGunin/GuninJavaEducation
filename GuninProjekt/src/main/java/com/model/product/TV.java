package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TV extends Product {
    private final String model;
    private final Manufacturer manufacturer;
    private final int diagonal;
    private List<String> details;
    private String currency;
    private LocalDateTime created;
    private OperatingSystem operatingSystem;

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

    public TV(String title, int count, double price, String currency,
              String model, Manufacturer manufacturer, int diagonal,
              LocalDateTime created, OperatingSystem operatingSystem) {
        super(title, count, price, ProductType.TV);
        this.model = model;
        this.manufacturer = manufacturer;
        this.diagonal = diagonal;
        this.currency = currency;
        this.created = created;
        this.operatingSystem = operatingSystem;
    }

    @Override
    public String toString() {
        return "TV{" +
                "id='" + id + '\'' +
                ", manufacturer=" + manufacturer +
                ", model=" + model + ",\n" +
                "created=" + created +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + String.format("%.2f", price) +
                ", diagonal" + diagonal +
                ", operating System = " + operatingSystem.getDesignation() + " " + operatingSystem.getVersion() +
                "}\n";
    }
}
