package com.command;

import com.model.product.Product;
import com.model.product.ProductType;
import com.model.product.UpdateParameter;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Update implements Command {

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String WHAT_PARAMETER = "What parameter do you want to update:";

    @Override
    public boolean execute() {
        String whatTypeUpdate = "What do you want to update:";
        final ProductType[] values = ProductType.values();
        final List<String> names = Utils.getNamesOfType(values);
        final int userTypeInput = Utils.getUserInput(names, whatTypeUpdate);
        if (userTypeInput != -1) {
            List<String> stringProducts = Utils.getProductStringList(values[userTypeInput]);
            return updateProduct(stringProducts, values[userTypeInput]);
        }
        return false;
    }

    private static boolean updateProduct(List<String> stringProducts, ProductType productType) {
        final int userInput;
        String whatProduct = "What product do you want to update:";
        userInput = Utils.getUserInput(stringProducts, whatProduct);
        if (userInput != -1) {
            return switch (productType) {
                case PHONE -> updateParameter(PHONE_SERVICE.useProductWithIndex(userInput));
                case TOASTER -> updateParameter(TOASTER_SERVICE.useProductWithIndex(userInput));
                case TV -> updateParameter(TV_SERVICE.useProductWithIndex(userInput));
            };
        }
        return false;
    }

    private static boolean updateParameter(Product product) {
        final int userInputParameter;
        final UpdateParameter[] values = UpdateParameter.values();
        final List<String> names = getNamesOfParameter(values);
        userInputParameter = Utils.getUserInput(names, WHAT_PARAMETER);
        if (userInputParameter != -1) {
            switch (values[userInputParameter]) {
                case COUNT -> updateCount(product);
                case PRICE -> updatePrice(product);
                case TITLE -> updateTitle(product);
                default -> throw new IllegalArgumentException("Unknown Parameter " + userInputParameter);
            }
            return true;
        }
        return false;
    }

    private static void updateCount(Product product) {
        try {
            System.out.println("Enter Count:");
            int input = SCANNER.nextInt();
            if (input >= 0) {
                product.setCount(input);
            }
        } catch (NumberFormatException e) {
            System.out.println("Input is not valid\n");
            System.out.println(WHAT_PARAMETER);
        }
    }

    private static void updatePrice(Product product) {
        try {
            System.out.println("Enter Price:");
            SCANNER.useLocale(Locale.US);
            double input = SCANNER.nextDouble();
            if (input >= 0) {
                product.setPrice(input);
            }
        } catch (NumberFormatException e) {
            System.out.println("Input is not valid\n");
            System.out.println(WHAT_PARAMETER);
        }
    }

    private static void updateTitle(Product product) {
        System.out.println("Enter Title:");
        String input = SCANNER.nextLine();
        product.setTitle(input);
    }

    private static List<String> getNamesOfParameter(final UpdateParameter[] values) {
        return Arrays.stream(values)
                .map(UpdateParameter::name)
                .collect(Collectors.toList());
    }
}
