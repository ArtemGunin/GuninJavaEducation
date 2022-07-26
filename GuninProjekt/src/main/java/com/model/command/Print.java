package com.model.command;

import com.model.product.ProductType;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

import java.util.List;

public class Print implements Command {

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();

    @Override
    public boolean execute() {
        String whatPrint = "What do you want to print:";
        final ProductType[] values = ProductType.values();
        final List<String> names = Utils.getNamesOfType(values);
        final int userInput = Utils.getUserInput(names, whatPrint);
        if (userInput != 0) {
            switch (values[userInput - 1]) {
                case PHONE -> PHONE_SERVICE.printAll();
                case TOASTER -> TOASTER_SERVICE.printAll();
                case TV -> TV_SERVICE.printAll();
                default -> throw new IllegalArgumentException("Unknown ProductType " + values[userInput]);
            }
            return true;
        }
        return false;
    }
}
