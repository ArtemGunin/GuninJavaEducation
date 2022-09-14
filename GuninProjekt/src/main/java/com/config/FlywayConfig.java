package com.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/hibernate";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";
    private static final String SCHEMAS = "public";
    private static final String LOCATIONS = "db.migration";

    public static Flyway getFlywayConfig() {
        return Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .baselineOnMigrate(true)
                .locations(LOCATIONS)
                .schemas(SCHEMAS)
                .load();
    }
}
