package com.example;

import com.example.service.ProductUtils;

public class Main {
    public static void main(String[] args) {
        int countOfCreatedProducts = 7;
        ProductUtils utils = new ProductUtils();
        utils.createAndSaveProducts(countOfCreatedProducts);
        utils.printProducts();
        utils.printCountSendingNotifiable();
    }
}
