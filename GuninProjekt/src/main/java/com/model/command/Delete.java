package com.model.command;

import com.model.product.ProductType;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

import java.util.List;

public class Delete implements Command {

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();

    @Override
    public boolean execute() {
        String whatTypeProduct = "What type product do you want to delete:";
        final ProductType[] values = ProductType.values();
        final List<String> names = Utils.getNamesOfType(values);
        final int userTypeInput = Utils.getUserInput(names, whatTypeProduct);
        if (userTypeInput != 0) {
            List<String> stringProducts = Utils.getProductStringList(values[userTypeInput - 1]);
            return deleteProduct(stringProducts, values[userTypeInput - 1]);
        }
        return false;
    }

    private static boolean deleteProduct(List<String> stringProducts, ProductType productType) {
        final int userInput;
        String whatProduct = "What product do you want to delete:";
        System.out.println(whatProduct);
        userInput = Utils.getUserInput(stringProducts, whatProduct);
        if (userInput != 0) {
            switch (productType) {
                case PHONE -> PHONE_SERVICE.deleteProduct(PHONE_SERVICE.useProductWithIndex(userInput - 1));
                case TOASTER -> TOASTER_SERVICE.deleteProduct(TOASTER_SERVICE.useProductWithIndex(userInput - 1));
                case TV -> TV_SERVICE.deleteProduct(TV_SERVICE.useProductWithIndex(userInput - 1));
                default -> throw new IllegalArgumentException("Unknown Product " + productType);
            }
            return true;
        }
        return false;
    }
}
