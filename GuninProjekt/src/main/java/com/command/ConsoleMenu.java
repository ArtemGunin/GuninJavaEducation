package com.command;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleMenu {

    public static void menu() throws IOException {
        boolean exit = false;
        String operation = "Choose a product operation:";
        final Commands[] values = Commands.values();
        final List<String> names = getNamesOfCommands(values);
        do {
            final int userCommand = Utils.getUserInput(names, operation);
            if (userCommand == -1) {
                exit = true;
            } else {
                Command command = values[userCommand].getCommand();
                if (!command.execute()) {
                    exit = true;
                }
            }
        } while (!exit);
    }

    private static List<String> getNamesOfCommands(final Commands[] values) {
        return Arrays.stream(values)
                .map(Commands::getName)
                .collect(Collectors.toList());
    }
}
