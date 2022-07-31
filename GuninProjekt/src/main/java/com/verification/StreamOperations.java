package com.verification;

import com.model.product.Manufacturer;
import com.model.product.Phone;
import com.model.product.Product;
import com.model.product.ProductType;
import com.service.PhoneService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamOperations {

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();

    public void operations() {
        PHONE_SERVICE.createAndSaveProducts(10);
        System.out.println("***".repeat(10));

        PHONE_SERVICE.printProductsExpensiveThen(600.0);
        System.out.println("***".repeat(10));

        System.out.println("Counts summary: " + PHONE_SERVICE.SumCounts());
        System.out.println("***".repeat(10));

        System.out.println("Map collection");
        Map<String, ProductType> productMap = PHONE_SERVICE.sortByTitleClearDuplicatesConvertToMap();
        for (Map.Entry<String, ProductType> entry : productMap.entrySet()) {
            System.out.println("Key - " + entry.getKey() + ", Value - " + entry.getValue());
        }
        System.out.println("***".repeat(10));

        System.out.println("Products without details:");
        System.out.println("Detail is present - " + PHONE_SERVICE.detailIsPresent("camera"));
        String[] detailsWithoutEqual = {"bottom", "screen", "processor", "video card"};
        ArrayList<String> detailListWithoutEqual = new ArrayList<>(List.of(detailsWithoutEqual));
        PHONE_SERVICE.saveProduct(new Phone(
                "Custom", 10, 100.0, "Model", Manufacturer.SONY, detailListWithoutEqual));
        System.out.println("Products with details, but without equal detail:");
        System.out.println("Detail is present - " + PHONE_SERVICE.detailIsPresent("camera"));
        String[] details = {"bottom", "screen", "processor", "camera", "video card"};
        ArrayList<String> detailList = new ArrayList<>(List.of(details));
        PHONE_SERVICE.saveProduct(new Phone(
                "Custom", 10, 100.0, "Model", Manufacturer.SONY, detailList));
        System.out.println("Products with details, and with equal detail:");
        System.out.println("Detail is present - " + PHONE_SERVICE.detailIsPresent("camera"));
        System.out.println("***");

        System.out.println(PHONE_SERVICE.getPriceSummaryStatistic());
        System.out.println("***".repeat(10));

        System.out.println("Do all products have a price?");
        System.out.println("All products with price - " + PHONE_SERVICE.allProductsHasPrice());
        PHONE_SERVICE.useProductWithIndex(0).setPrice(0.0);
        System.out.println("One product has no price - " + PHONE_SERVICE.allProductsHasPrice());
        System.out.println("***".repeat(10));

        Map<String, Object> productContainer = new HashMap<>();
        productContainer.put("productType", ProductType.PHONE);
        productContainer.put("title", "Title - 357");
        productContainer.put("count", 124);
        productContainer.put("price", 562.12);
        productContainer.put("model", "Model - 563");
        productContainer.put("manufacturer", Manufacturer.SAMSUNG);
        Product product = PHONE_SERVICE.createProductFromMap(productContainer);
        System.out.println("Created product: \n" + product);
        System.out.println("***".repeat(10));
    }
}
