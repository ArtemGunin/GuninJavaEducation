package com;

import com.config.FlywayConfig;
import com.config.HibernateFactoryUtils;
import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;

import java.io.IOException;

public class Main {

    @SneakyThrows
    public static void main(String[] args) throws IOException {

        Class.forName("org.postgresql.Driver");

        Flyway flyway = FlywayConfig.getFlywayConfig();

        flyway.clean();

        HibernateFactoryUtils.getSessionFactory();

        flyway.migrate();
    }
}
