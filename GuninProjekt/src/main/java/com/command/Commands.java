package com.command;

import lombok.Getter;

@Getter
public enum Commands {
    CREATE("Create product", new Create()),
    UPDATE("Update product", new Update()),
    DELETE("Delete product", new Delete()),
    PRINT("Print products", new Print()),
    CREATE_FROM_FILE("Create product from file", new CreateProductFromFile());

    private final String name;
    private final Command command;

    Commands(String name, Command command) {
        this.name = name;
        this.command = command;
    }
}
