package com.verification;

import com.model.product.Manufacturer;
import com.model.product.TV;
import com.service.ProductContainer;

public class ProductContainerOperations {
    private static final ProductContainer<TV> productContainer
            = new ProductContainer<>(new TV("Title", 10, 100.0,
            "Model", Manufacturer.SONY, 32));

    public void containerOperations() {
        System.out.println("\nInside product:");
        productContainer.showInformation();
        productContainer.useDiscount();
        System.out.println("\nInside product after using discount:");
        productContainer.showInformation();
        productContainer.increaseProductCount(50);
        System.out.println("\nInside product after increased product count:");
        productContainer.showInformation();
    }
}
