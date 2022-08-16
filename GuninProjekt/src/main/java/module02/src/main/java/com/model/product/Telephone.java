package com.model.product;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Telephone extends Product {
    private String model;
    private String series;
    private ScreenType screenType;
    private long price;
    private ProductType productType;

    private Telephone(String model, String series, ScreenType screenType, long price) {
        this.series = series;
        this.screenType = screenType;
        this.price = price;
        this.productType = ProductType.TELEPHONE;
        this.model = model;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "model='" + model + '\'' +
                ", series='" + series + '\'' +
                ", screenType=" + screenType +
                ", price=" + price +
                '}';
    }

    public static class TelephoneBuilder {
        private String model;
        private String series;
        private ScreenType screenType;
        private long price;

        public TelephoneBuilder setModel(String model) {
            this.model = model;
            return this;
        }

        public TelephoneBuilder setSeries(String series) {
            this.series = series;
            return this;
        }

        public TelephoneBuilder setScreenType(ScreenType screenType) {
            this.screenType = screenType;
            return this;
        }

        public TelephoneBuilder setPrice(long price) {
            this.price = price;
            return this;
        }

        public Telephone build() {
            if (price < 0.0) {
                throw new IllegalStateException("Price can not be less then 0");
            }
            return new Telephone(model, series, screenType, price);
        }
    }
}
