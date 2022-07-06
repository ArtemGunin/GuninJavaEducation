package com.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Phone extends Product {
    private final String model;
    private final PhoneManufacture phoneManufacturer;

    public Phone(String title, int count, double price,
                 String model, PhoneManufacture phoneManufacturer) {
        super(title, count, price);
        this.model = model;
        this.phoneManufacturer = phoneManufacturer;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "manufacturer=" + phoneManufacturer +
                ", model=" + model +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
