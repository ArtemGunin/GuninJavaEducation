package com.model.command;

import com.model.product.Product;
import com.model.product.ProductType;
import com.model.product.UpdateParameter;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Update implements Command {

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean execute() {
        String whatTypeUpdate = "What do you want to update:";
        final ProductType[] values = ProductType.values();
        final List<String> names = Utils.getNamesOfType(values);
        final int userTypeInput = Utils.getUserInput(names, whatTypeUpdate);
        if (userTypeInput != 0) {
            List<String> stringProducts = Utils.getProductStringList(values[userTypeInput - 1]);
            return updateProduct(stringProducts, values[userTypeInput - 1]);
        }
        return false;
    }

    private static boolean updateProduct(List<String> stringProducts, ProductType productType) {
        final int userInput;
        String whatProduct = "What product do you want to update:";
        userInput = Utils.getUserInput(stringProducts, whatProduct);
        if (userInput != 0) {
            switch (productType) {
                case PHONE -> {
                    return updateParameter(PHONE_SERVICE.useProductWithIndex(userInput - 1));
                }
                case TOASTER -> {
                    return updateParameter(TOASTER_SERVICE.useProductWithIndex(userInput - 1));
                }
                case TV -> {
                    return updateParameter(TV_SERVICE.useProductWithIndex(userInput - 1));
                }
                default -> throw new IllegalArgumentException("Unknown Product " + productType);
            }
        }
        return false;
    }

    private static boolean updateParameter(Product product) {
        final int userInputParameter;
        String whatParameter = "What parameter do you want to update:";
        final UpdateParameter[] values = UpdateParameter.values();
        final List<String> names = getNamesOfParameter(values);
        userInputParameter = Utils.getUserInput(names, whatParameter);
        if (userInputParameter != 0) {
            switch (values[userInputParameter - 1]) {
                case COUNT -> {
                    try {
                        System.out.println("Enter Count:");
                        int input = scanner.nextInt();
                        if (input >= 0) {
                            product.setCount(input);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input is not valid\n");
                        System.out.println(whatParameter);
                    }
                }
                case PRICE -> {
                    try {
                        System.out.println("Enter Price:");
                        scanner.useLocale(Locale.US);
                        double input = scanner.nextDouble();
                        if (input >= 0) {
                            product.setPrice(input);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input is not valid\n");
                        System.out.println(whatParameter);
                    }
                }
                case TITLE -> {
                    System.out.println("Enter Title:");
                    String input = scanner.nextLine();
                    product.setTitle(input);
                }
                default -> throw new IllegalArgumentException("Unknown Parameter " + userInputParameter);
            }
            return true;
        }
        return false;
    }

    private static List<String> getNamesOfParameter(final UpdateParameter[] values) {
        final List<String> names = new ArrayList<>(values.length);
        for (UpdateParameter type : values) {
            names.add(type.name());
        }
        return names;
    }
}
