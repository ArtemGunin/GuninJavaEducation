package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
        this(UUID.randomUUID().toString(), title, count, price, "", model, manufacturer,
                Collections.emptyList(), diagonal, LocalDateTime.now(), new OperatingSystem("", 0));
    }

    public TV(String id, String title, int count, double price,
              String model, Manufacturer manufacturer, int diagonal) {
        this(id, title, count, price, "", model, manufacturer,
                Collections.emptyList(), diagonal, LocalDateTime.now(), new OperatingSystem("", 0));
    }

    public TV(String title, int count, double price, String model,
              Manufacturer manufacturer, int diagonal, List<String> details) {
        this(UUID.randomUUID().toString(), title, count, price, "", model, manufacturer,
                Collections.emptyList(), diagonal, LocalDateTime.now(), new OperatingSystem("", 0));
    }

    public TV(String title, int count, double price, String currency,
              String model, Manufacturer manufacturer, int diagonal,
              LocalDateTime created, OperatingSystem operatingSystem) {
        this(UUID.randomUUID().toString(), title, count, price, currency, model,
                manufacturer, Collections.emptyList(), diagonal, created, operatingSystem);
    }

    public TV(String id, String title, int count, double price, String currency,
              String model, Manufacturer manufacturer, List<String> details, int diagonal,
              LocalDateTime created, OperatingSystem operatingSystem) {
        super(id, title, count, price, ProductType.TV);
        this.model = model;
        this.manufacturer = manufacturer;
        this.diagonal = diagonal;
        this.currency = currency;
        this.details = details;
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
