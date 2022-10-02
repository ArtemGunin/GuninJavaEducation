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
public class TV extends Product {
    private String model;
    private Manufacturer manufacturer;
    private int diagonal;
    @Transient
    private transient List<String> details;
    private String currency;
    private LocalDateTime created;
    @Transient
    private transient OperatingSystem operatingSystem;

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
        String OS = "Unknown";
        if (operatingSystem != null) {
            OS = operatingSystem.getDesignation() + " " + operatingSystem.getVersion();
        }
        return "TV{" +
                "id='" + id + '\'' +
                ", manufacturer=" + manufacturer +
                ", model=" + model + ",\n" +
                "created=" + created +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + String.format("%.2f", price) +
                ", diagonal=" + diagonal +
                ", operating System=" + OS +
                "}\n";
    }
}
