package com.command;

import com.repository.InvoiceRepository;
import com.service.AnalyticalUtils;
import com.service.ShopService;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SetLimit implements Command {

    private static final ShopService SHOP_SERVICE = new ShopService(InvoiceRepository.getInstance());
    private static final AnalyticalUtils ANALYTICAL_UTILS = new AnalyticalUtils(InvoiceRepository.getInstance());
    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public boolean execute() {
        String enterLimit = "Please enter the limit of cost: ";
        final long userInput = getUserInput(enterLimit);
        if (userInput != 0) {
            SHOP_SERVICE.createAndSaveInvoices(15, "products.csv", userInput);
            ANALYTICAL_UTILS.printAnalyticalData();
        }
        return false;
    }

    private static long getUserInput(String operation) {
        long userChoice = -1;
        do {
            try {
                System.out.println(operation + "\nEnter \"0\" to Exit.");
                final String line = READER.readLine();
                if (!StringUtils.isNumeric(line)) {
                    System.out.println("Input is not valid\n");
                    continue;
                }
                userChoice = Long.parseLong(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (userChoice == -1);
        return userChoice;
    }
}
