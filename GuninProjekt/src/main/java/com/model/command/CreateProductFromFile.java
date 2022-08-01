package com.model.command;

import com.model.product.*;
import com.service.PhoneService;
import com.service.ProductService;
import com.service.TVService;
import com.service.ToasterService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateProductFromFile implements Command {

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();
    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public boolean execute() {
        String createFrom = "Enter the file name you want to create the product from:";
        int userInput = -1;
        Product product = null;
        do {
            try {
                System.out.println(createFrom + "\nTo exit press \"0\"");
                String file = READER.readLine();
                if (!file.equals("0")) {
                    ProductType productType = ProductType.valueOf(ProductService.getProductType(file));
                    switch (productType) {
                        case PHONE -> {
                            Phone phone = PHONE_SERVICE.createProductFromFile(file);
                            PHONE_SERVICE.saveProduct(phone);
                            product = phone;
                        }
                        case TOASTER -> {
                            Toaster toaster = TOASTER_SERVICE.createProductFromFile(file);
                            TOASTER_SERVICE.saveProduct(toaster);
                            product = toaster;
                        }
                        case TV -> {
                            TV tv = TV_SERVICE.createProductFromFile(file);
                            TV_SERVICE.saveProduct(tv);
                            product = tv;
                        }
                        default -> throw new IllegalArgumentException("Unknown Product " + productType);
                    }
                } else {
                    userInput = 0;
                }
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (product != null) {
                System.out.println("\nCreated product: \n" + product + "\n");
                return true;
            }
        } while (userInput == -1);
        return false;
    }
}
