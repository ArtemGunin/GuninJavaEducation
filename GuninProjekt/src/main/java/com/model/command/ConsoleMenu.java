package com.model.command;

import java.util.ArrayList;
import java.util.List;

public class ConsoleMenu {

    public static void menu() {
        boolean exit = false;
        String operation = "Choose a product operation:";
        final Commands[] values = Commands.values();
        final List<String> names = getNamesOfCommands(values);
        do {
            final int userCommand = Utils.getUserInput(names, operation);
            if (userCommand == 0) {
                exit = true;
            } else {
                Command command = values[userCommand - 1].getCommand();
                if (!command.execute()) {
                    exit = true;
                }
            }
        } while (!exit);
    }

    private static List<String> getNamesOfCommands(final Commands[] values) {
        final List<String> names = new ArrayList<>(values.length);
        for (Commands type : values) {
            names.add(type.getName());
        }
        return names;
    }
}
