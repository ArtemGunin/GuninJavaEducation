package com.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Toaster extends Product {
    private String model;
    private int power;
    private Manufacturer manufacturer;
    @Transient
    private transient List<String> details;
    private String currency;
    private LocalDateTime created;
    @Transient
    private transient Body body;

    private Toaster(String id, String title, int count, double price, String currency,
                    String model, int power, Manufacturer manufacturer,
                    LocalDateTime created, Body body, List<String> details) {
        super(id, title, count, price, ProductType.TOASTER);
        this.model = model;
        this.power = power;
        this.manufacturer = manufacturer;
        this.currency = currency;
        this.created = created;
        this.body = body;
        this.details = details;
    }

    @Override
    public String toString() {
        String corpus = "Unknown";
        if (body != null) {
            corpus = body.getColor() + " " + body.getMaterial();
        }

        return "Toaster{" +
                "id='" + id + '\'' +
                ", manufacturer=" + manufacturer +
                ", model=" + model + ",\n" +
                "created=" + created +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + String.format("%.2f", price) +
                ", power=" + power +
                ", body=" + corpus +
                "}\n";
    }

    public static class ToasterBuilder {

        private String id;
        private String title;
        private int count;
        private double price;
        private String model;
        private int power;
        private Manufacturer manufacturer;
        private List<String> details;
        private String currency;
        private LocalDateTime created;
        private Body body;

        public ToasterBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public ToasterBuilder setTitle(String title) {
            if (title.length() > 20) {
                throw new IllegalArgumentException("Title length must be less than 20 characters");
            }
            this.title = title;
            return this;
        }

        public ToasterBuilder setCount(int count) {
            if (count < 0) {
                throw new IllegalArgumentException("Count must be more than 0 characters");
            }
            this.count = count;
            return this;
        }

        public ToasterBuilder setPrice(double price) {
            this.price = price;
            return this;
        }

        public ToasterBuilder setModel(String model) {
            this.model = model;
            return this;
        }

        public ToasterBuilder setPower(int power) {
            this.power = power;
            return this;
        }

        public ToasterBuilder setManufacturer(Manufacturer manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public ToasterBuilder setDetails(List<String> details) {
            this.details = details;
            return this;
        }

        public ToasterBuilder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public ToasterBuilder setCreated(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public ToasterBuilder setBody(Body body) {
            if (body == null) {
                body = new Body("", "");
            }
            this.body = body;
            return this;
        }

        public ToasterBuilder setLocalDateTime(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public Toaster build() {
            if (price < 0.0) {
                throw new IllegalStateException("Price must be present");
            }
            if (body == null) {
                body = new Body("", "");
            }
            if (id == null) {
                this.id = UUID.randomUUID().toString();
            }
            return new Toaster(id, title, count, price, currency,
                    model, power, manufacturer, created, body, details);
        }
    }
}
