package com.command;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleMenu {

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public static void menu() throws IOException {
        boolean exit = false;
        String operation = "Choose operation:";
        final Commands[] values = Commands.values();
        final List<String> names = getNamesOfCommands(values);
        do {
            final int userCommand = getUserInput(names, operation);
            if (userCommand == 0) {
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
        final List<String> names = new ArrayList<>(values.length);
        for (Commands type : values) {
            names.add(type.getName());
        }
        return names;
    }

    private static int getUserInput(List<String> names, String operation) {
        int length = names.size();
        inputPrint(names, operation);
        int userChoice = -1;
        do {
            try {
                final String line = READER.readLine();
                userChoice = checkInputValue(line, length);
            } catch (IOException | NumberFormatException e) {
                System.out.println("Input is not valid\n");
            }
        } while (userChoice == -1);
        return userChoice;
    }

    private static void inputPrint(List<String> names, String operation) {
        int length = names.size();
        System.out.println(operation);
        System.out.println("Please enter number between 1 and " + (length - 1) + ".");
        for (int i = 1; i < length; i++) {
            System.out.printf("%d) %s%n", i, names.get(i));
        }
        System.out.println("Enter \"0\" to Exit.");
    }

    private static int checkInputValue(String line, int length) {
        int input = Integer.parseInt(line);
        if (!StringUtils.isNumeric(line)) {
            System.out.println("Input is not valid\n");
            return -1;
        } else if (input < 0 || input > length) {
            System.out.println("Incorrect number!!!\n");
            return -1;
        }
        return input;
    }
}
