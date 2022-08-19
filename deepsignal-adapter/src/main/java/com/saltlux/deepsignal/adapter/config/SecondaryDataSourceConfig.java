package com.saltlux.deepsignal.adapter.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.saltlux.deepsignal.adapter.secondary.domain")
@EnableJpaRepositories(
    transactionManagerRef = "secondaryTransactionManager",
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    basePackages = "com.saltlux.deepsignal.adapter.secondary.repository"
)
public class SecondaryDataSourceConfig {

    @Bean
    @ConfigurationProperties("partner.datasource.secondary")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("partner.datasource.secondary")
    public DataSource secondaryDataSource() {
        return secondaryDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDB103Dialect");

        LocalContainerEntityManagerFactoryBean emf = builder
            .dataSource(secondaryDataSource())
            .packages("com.saltlux.deepsignal.adapter.secondary.domain")
            .persistenceUnit("secondary")
            .build();
        emf.setJpaProperties(properties);
        return emf;
    }

    @Bean(name = "secondaryTransactionManager")
    public JpaTransactionManager db2TransactionManager(@Qualifier("secondaryEntityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
