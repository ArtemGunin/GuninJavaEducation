package com.example.service;

import com.example.model.ProductImpl;

import java.util.Random;

public class ProductUtils {
    private static final ProductService PRODUCT_SERVICE = ProductService.getInstance();

    private static final ProductBundleService PRODUCT_BUNDLE_SERVICE
            = new ProductBundleService(PRODUCT_SERVICE.repository);

    private static final NotifiableProductService NOTIFIABLE_PRODUCT_SERVICE
            = new NotifiableProductService(PRODUCT_SERVICE.repository);

    private ProductImpl generateRandomProduct() {
        Random random = new Random();
        if (random.nextBoolean()) {
            return PRODUCT_BUNDLE_SERVICE.createRandomProduct();
        } else {
            return NOTIFIABLE_PRODUCT_SERVICE.createRandomProduct();
        }
    }

    public void createAndSaveProducts(int count) {
        while (count > 0) {
            PRODUCT_SERVICE.repository.save(generateRandomProduct());
            count--;
        }
    }

    public void printProducts() {
        System.out.println(PRODUCT_SERVICE.repository.getAll());
    }

    public void printCountSendingNotifiable() {
        System.out.println("notifications sent: " +
                NOTIFIABLE_PRODUCT_SERVICE.filterNotifiableProductsAndSendNotifications());
    }
}
