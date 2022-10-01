package com;

import com.verification.OperationsDB;

import java.io.IOException;
import java.util.Random;

public class Main {
    protected static final Random RANDOM = new Random();

    @SneakyThrows
    public static void main(String[] args) throws IOException {

        new OperationsDB().run();

    }
}
