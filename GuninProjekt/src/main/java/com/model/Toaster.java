package com.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Toaster extends Product {
    private final String model;
    private final int power;
    private final ToasterManufacture toasterManufacture;

    public Toaster(String title, int count, double price, String model,
            int power, ToasterManufacture toasterManufacture) {
        super(title, count, price);
        this.model = model;
        this.power = power;
        this.toasterManufacture = toasterManufacture;
    }

    @Override
    public String toString() {
        return "Toaster{" +
                "manufacturer=" + toasterManufacture +
                ", model=" + model +
                ", power=" + power +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
