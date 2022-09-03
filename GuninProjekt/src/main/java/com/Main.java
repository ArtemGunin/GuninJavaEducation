package com;

import com.config.FlywayConfig;
import org.flywaydb.core.Flyway;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Flyway flyway = FlywayConfig.getFlywayConfig();

        flyway.migrate();
    }
}
