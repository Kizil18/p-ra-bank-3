package com.bank.history;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestBean {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:latest");
    }

    @DynamicPropertySource
    static void postgresSqlProperties (DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.hikari.schema", () -> "history");
    }

}
