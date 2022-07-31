package com.service;

import com.model.product.Phone;
import com.model.product.ProductType;
import com.model.product.TV;
import com.model.product.Toaster;

public class ProductFactory {

    private static final ProductService<Phone> PHONE_SERVICE = PhoneService.getInstance();
    private static final ProductService<Toaster> TOASTER_SERVICE = ToasterService.getInstance();
    private static final ProductService<TV> TV_SERVICE = TVService.getInstance();

    private ProductFactory() {
    }

    public static void createAndSave(ProductType type) {
        switch (type) {
            case PHONE -> PHONE_SERVICE.createAndSaveProducts(1);
            case TOASTER -> TOASTER_SERVICE.createAndSaveProducts(1);
            case TV -> TV_SERVICE.createAndSaveProducts(1);
            default -> throw new IllegalArgumentException("Unknown product type: " + type);
        }
    }
}
