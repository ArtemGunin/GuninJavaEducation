package com.example.model;

import lombok.Data;

@Data
public abstract class ProductImpl implements Product {
    protected long id;
    protected ProductType type;
    protected boolean available;
    protected String title;
    protected double price;

    public String getBasicInfo() {
        return "Product{" +
                "id=" + id +
                ", available=" + available +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
