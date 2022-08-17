package com.command;

import com.model.product.ProductType;
import com.service.ProductFactory;

import java.util.List;

public class Create implements Command {

    @Override
    public boolean execute() {
        String whatProduct = "What product do you want to create:";
        final ProductType[] values = ProductType.values();
        final List<String> names = Utils.getNamesOfType(values);
        final int userInput = Utils.getUserInput(names, whatProduct);
        if (userInput != -1) {
            ProductFactory.createAndSave(values[userInput]);
            return true;
        }
        return false;
    }
}
