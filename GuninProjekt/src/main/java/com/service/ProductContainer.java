package com.service;

import com.model.product.Product;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

@Getter
@Setter
public class ProductContainer<T extends Product> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductContainer.class);

    private final T product;
    private static final Random RANDOM = new Random();

    public ProductContainer(T product) {
        this.product = product;
    }

    public void showInformation() {
        LOGGER.info(product.toString());
    }

    public void useDiscount() {
        int lowDiscountPercentBound = 10;
        int hiDiscountPercentBound = 31;
        double productPrice = product.getPrice();
        int discount = lowDiscountPercentBound
                + RANDOM.nextInt(hiDiscountPercentBound - lowDiscountPercentBound);
        this.product.setPrice(productPrice - (productPrice * discount / 100));
    }

    public <X extends Number> void increaseProductCount(X increaseCount) {
        product.setCount(product.getCount() + increaseCount.intValue());
    }
}
