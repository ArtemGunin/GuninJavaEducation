package com.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Phone extends Product {
    private String model;
    private Manufacturer manufacturer;
    @Transient
    private transient List<String> details;
    @Transient
    private transient String currency;
    private transient LocalDateTime created;
    @Transient
    private transient OperatingSystem operatingSystem;

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
        String OS = "Unknown";
        if (operatingSystem != null) {
            OS = operatingSystem.getDesignation() + " " + operatingSystem.getVersion();
        }
        return "Phone{" +
                "id='" + id + '\'' +
                ", manufacturer=" + manufacturer +
                ", model=" + model + ",\n" +
                "created=" + created +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + String.format("%.2f", price) +
                ", operating System=" + OS +
                "}\n";
    }
}
