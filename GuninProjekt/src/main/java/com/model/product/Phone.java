package com.model.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Phone extends Product {
    private final String model;
    private final Manufacturer manufacturer;
    private List<String> details;
    private String currency;
    private LocalDateTime created;
    private OperatingSystem operatingSystem;

    public Phone(String title, int count, double price,
                 String model, Manufacturer manufacturer) {
        this(UUID.randomUUID().toString(), title, count, price, "", model,
                manufacturer, Collections.emptyList(), LocalDateTime.now(), new OperatingSystem("", 0));
    }

    public Phone(String id, String title, int count, double price,
                 String model, Manufacturer manufacturer) {
        this(id, title, count, price, "", model,
                manufacturer, Collections.emptyList(), LocalDateTime.now(), new OperatingSystem("", 0));
    }

    public Phone(String title, int count, double price,
                 String model, Manufacturer manufacturer, List<String> details) {
        this(UUID.randomUUID().toString(), title, count, price, "", model,
                manufacturer, details, LocalDateTime.now(), new OperatingSystem("", 0));
    }

    public Phone(String title, int count, double price, String currency, String model,
                 Manufacturer manufacturer, LocalDateTime created, OperatingSystem operatingSystem) {
        this(UUID.randomUUID().toString(), title, count, price, currency, model,
                manufacturer, Collections.emptyList(), created, operatingSystem);
    }

    public Phone(String id, String title, int count, double price, String currency, String model,
                 Manufacturer manufacturer, List<String> details, LocalDateTime created,
                 OperatingSystem operatingSystem) {
        super(id, title, count, price, ProductType.PHONE);
        this.model = model;
        this.manufacturer = manufacturer;
        this.currency = currency;
        this.details = details;
        this.created = created;
        this.operatingSystem = operatingSystem;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id='" + id + '\'' +
                ", manufacturer=" + manufacturer +
                ", model=" + model + ",\n" +
                "created=" + created +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + String.format("%.2f", price) +
                ", operating System = " + operatingSystem.getDesignation() + " " + operatingSystem.getVersion() +
                "}\n";
    }
}
