package com.model.product;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Television extends Product {
    private String series;
    private ScreenType screenType;
    private double diagonal;
    private String country;
    private long price;
    private ProductType productType;

    private Television(String series, ScreenType screenType, double diagonal, String country, long price) {
        this.series = series;
        this.screenType = screenType;
        this.price = price;
        this.productType = ProductType.TELEVISION;
        this.diagonal = diagonal;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Television{" +
                "series='" + series + '\'' +
                ", screenType=" + screenType +
                ", diagonal=" + diagonal +
                ", country='" + country + '\'' +
                ", price=" + price +
                '}';
    }

    public static class TelevisionBuilder {
        private String series;
        private ScreenType screenType;
        private double diagonal;
        private String country;
        private long price;


        public TelevisionBuilder setSeries(String series) {
            this.series = series;
            return this;
        }

        public TelevisionBuilder setScreenType(ScreenType screenType) {
            this.screenType = screenType;
            return this;
        }

        public TelevisionBuilder setDiagonal(double diagonal) {
            this.diagonal = diagonal;
            return this;
        }

        public TelevisionBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public TelevisionBuilder setPrice(long price) {
            this.price = price;
            return this;
        }

        public Television build() {
            if (price < 0.0) {
                throw new IllegalStateException("Price can not be less then 0");
            }
            return new Television(series, screenType, diagonal, country, price);
        }
    }
}
