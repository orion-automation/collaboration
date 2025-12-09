package com.eorion.bo.enhancement.collaboration.config;

import jakarta.annotation.PostConstruct;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.oracle.OracleContainer;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@Slf4j
public class TestContainersDataSourceConfiguration {
    static String image = "gvenzl/oracle-free:23.6-slim-faststart";

    @PostConstruct
    public void init() {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "testcontainers");
    }

    @Container
    static OracleContainer oracleContainer = new OracleContainer(image)
            .withStartupTimeout(Duration.ofMinutes(3))
            .withUsername("testuser")
            .withPassword("testpwd");

    static {
        oracleContainer.start();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        log.info("Creating Oracle DataSource");
        log.info("Driver class name: {}", oracleContainer.getDriverClassName());
        log.info("Database URL: {}", oracleContainer.getJdbcUrl());
        log.info("Database user: {}", oracleContainer.getUsername());
        log.info("Database password: {}", oracleContainer.getPassword());

        return DataSourceBuilder.create()
                .driverClassName(oracleContainer.getDriverClassName())
                .url(oracleContainer.getJdbcUrl())
                .username(oracleContainer.getUsername())
                .password(oracleContainer.getPassword())
                .build();
    }

    @Bean
    @Primary
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:/db/collaboration-changelog.yml");
        liquibase.setShouldRun(true);

        return liquibase;
    }

    @Bean
    @Primary
    @DependsOn("liquibase")
    public PlatformTransactionManager primaryTransactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}
