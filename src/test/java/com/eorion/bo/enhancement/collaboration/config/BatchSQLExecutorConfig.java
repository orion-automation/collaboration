package com.eorion.bo.enhancement.collaboration.config;

import com.eorion.bo.enhancement.collaboration.utils.BatchSQLExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BatchSQLExecutorConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public BatchSQLExecutor executor() {
        return new BatchSQLExecutor(dataSource);
    }
}
