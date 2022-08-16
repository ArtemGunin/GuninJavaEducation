package com.command;

import lombok.Getter;

@Getter
public enum Commands {
    EXIT("Exit", null),
    SET_COST_LIMIT("Set the limit of cost: ", new SetLimit());

    private final String name;
    private final Command command;

    Commands(String name, Command command) {
        this.name = name;
        this.command = command;
    }
}
