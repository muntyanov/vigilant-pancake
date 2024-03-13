package ru.tinkoff.seminars.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestAccountingApplication {

    static PostgreSQLContainer postgres;

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
        return postgres;
    }

    @DynamicPropertySource
    static void setDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgres.getUsername());
        registry.add("spring.datasource.password", () -> postgres.getPassword());
    }

    public static void main(String[] args) {
        SpringApplication.from(AccountingApplication::main).with(TestAccountingApplication.class).run(args);
    }

}
