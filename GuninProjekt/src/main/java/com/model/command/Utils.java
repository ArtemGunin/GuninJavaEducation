package com.model.command;

import com.model.product.Phone;
import com.model.product.ProductType;
import com.model.product.TV;
import com.model.product.Toaster;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();

    private Utils() {
    }

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public static int getUserInput(List<String> names, String operation) {
        int length = names.size();
        int userChoice = -1;
        do {
            try {
                System.out.println(operation);
                System.out.println("Please enter number between 1 and " + length + ".");
                for (int i = 1; i <= length; i++) {
                    System.out.printf("%d) %s%n", i, names.get(i - 1));
                }
                System.out.println("Enter \"0\" to Exit.");
                int input = Integer.parseInt(READER.readLine());
                if (input >= 0 && input <= length) {
                    userChoice = input;
                } else {
                    System.out.println("Incorrect number!!!\n");
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Input is not valid\n");
            }
        } while (userChoice == -1);
        return userChoice;
    }

    public static List<String> getNamesOfType(final ProductType[] values) {
        final List<String> names = new ArrayList<>(values.length);
        for (ProductType type : values) {
            names.add(type.name());
        }
        return names;
    }

    public static List<String> getProductStringList(ProductType productType) {
        List<String> stringProducts = new ArrayList<>();
        switch (productType) {
            case PHONE -> {
                List<Phone> phones = PHONE_SERVICE.getAll();
                for (Phone phone : phones) {
                    stringProducts.add(phone.toString());
                }
            }
            case TOASTER -> {
                List<Toaster> toasters = TOASTER_SERVICE.getAll();
                for (Toaster toaster : toasters) {
                    stringProducts.add(toaster.toString());
                }
            }
            case TV -> {
                List<TV> tvs = TV_SERVICE.getAll();
                for (TV tv : tvs) {
                    stringProducts.add(tv.toString());
                }
            }
            default -> throw new IllegalArgumentException("Unknown ProductType " + productType);
        }
        return stringProducts;
    }
}
