package com.bncrypted.authenticator.configuration;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties("authenticator.api.datasource.postgres")
    public DataSource getPostgresDataSource() {
        return new PGSimpleDataSource();
    }

}
