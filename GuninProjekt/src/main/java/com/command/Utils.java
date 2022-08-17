package com.command;

import com.model.product.Product;
import com.model.product.ProductType;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();

    private Utils() {
    }

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public static int getUserInput(List<String> names, String operation) {
        int exitCompensation = 1;
        int length = names.size();
        int userChoice = -1;
        do {
            try {
                System.out.println(operation);
                System.out.println("Please enter number between 1 and " + length + ".");
                for (int i = 1; i <= length; i++) {
                    System.out.printf("%d) %s%n", i, names.get(i - exitCompensation));
                }
                System.out.println("Enter \"0\" to Exit.");
                final String line = READER.readLine();
                if (!StringUtils.isNumeric(line)) {
                    System.out.println("Incorrect number!!!\n");
                    continue;
                }
                int input = Integer.parseInt(line);
                if (input >= 0 && input <= length) {
                    userChoice = input;
                } else {
                    System.out.println("Incorrect number!!!\n");
                }
            } catch (IOException e) {
                System.out.println("Input is not valid\n");
            }
        } while (userChoice == -1);
        return userChoice - exitCompensation;
    }

    public static List<String> getNamesOfType(final ProductType[] values) {
        return Arrays.stream(values)
                .map(ProductType::name)
                .collect(Collectors.toList());
    }

    public static List<String> getProductStringList(ProductType productType) {
        List<? extends Product> products = switch (productType) {
            case PHONE -> PHONE_SERVICE.getAll();
            case TOASTER -> TOASTER_SERVICE.getAll();
            case TV -> TV_SERVICE.getAll();
        };
        return products.stream()
                .map(Product::toString)
                .collect(Collectors.toList());
    }
}
