package com.bank.history;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
public class TestBean {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>(
                "postgres:latest"
        );
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        hikariDataSource.setUsername(postgreSQLContainer.getUsername());
        hikariDataSource.setPassword(postgreSQLContainer.getPassword());
        hikariDataSource.setSchema("history");
        return hikariDataSource;
    }
}
