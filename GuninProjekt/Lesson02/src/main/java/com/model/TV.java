package com.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TV extends Product {
    private final String model;
    private final TVManufacture tvManufacturer;
    private final int diagonal;

    public TV(String title, int count, double price, String model,
              TVManufacture tvManufacture, int diagonal) {
        super(title, count, price);
        this.model = model;
        this.tvManufacturer = tvManufacture;
        this.diagonal = diagonal;
    }

    @Override
    public String toString() {
        return "TV{" +
                "manufacturer=" + tvManufacturer +
                ", model=" + model +
                ", diagonal=" + diagonal +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
