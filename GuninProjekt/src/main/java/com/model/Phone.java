package com.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Phone extends Product {
    private final String model;
    private final Manufacturer manufacturer;

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

    public Phone(String id, String title, int count, double price,
                 String model, PhoneManufacture phoneManufacturer) {
        super(id, title, count, price);
        this.model = model;
        this.phoneManufacturer = phoneManufacturer;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "manufacturer=" + manufacturer +
                ", model=" + model +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
