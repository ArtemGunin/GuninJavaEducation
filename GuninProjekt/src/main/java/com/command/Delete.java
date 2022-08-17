package com.command;

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
        if (userTypeInput != -1) {
            List<String> stringProducts = Utils.getProductStringList(values[userTypeInput]);
            return deleteProduct(stringProducts, values[userTypeInput]);
        }
        return false;
    }

    private static boolean deleteProduct(List<String> stringProducts, ProductType productType) {
        final int userInput;
        String whatProduct = "What product do you want to delete:";
        userInput = Utils.getUserInput(stringProducts, whatProduct);
        if (userInput != -1) {
            switch (productType) {
                case PHONE -> PHONE_SERVICE.deleteProduct(PHONE_SERVICE.useProductWithIndex(userInput));
                case TOASTER -> TOASTER_SERVICE.deleteProduct(TOASTER_SERVICE.useProductWithIndex(userInput));
                case TV -> TV_SERVICE.deleteProduct(TV_SERVICE.useProductWithIndex(userInput));
                default -> throw new IllegalArgumentException("Unknown Product " + productType);
            }
            return true;
        }
        return false;
    }
}
