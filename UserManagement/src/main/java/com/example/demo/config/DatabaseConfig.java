package com.example.demo.config;

import javax.sql.DataSource;

//import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class DatabaseConfig {

    @Primary
    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSource userDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory") // Renamed to this!
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("userDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.demo.entity")
                .persistenceUnit("userPU")
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager userTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

 // --- Secondary: Healthcare Database for Doctor & Patient records ---
 @Bean(name = "healthcareDataSource")
 @ConfigurationProperties(prefix = "spring.datasource.healthcare")
 public DataSource healthcareDataSource() {
     return DataSourceBuilder.create().build();
 }

 @Bean(name = "healthcareEntityManagerFactory")
 public LocalContainerEntityManagerFactoryBean healthcareEntityManagerFactory() {
     LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
     em.setDataSource(healthcareDataSource());
     // This package contains the Doctor and Patient entities
     em.setPackagesToScan("com.healthcare.entity");
     em.setPersistenceUnitName("healthcarePU");
     em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
     return em;
 }

 @Bean(name = "healthcareTransactionManager")
 public PlatformTransactionManager healthcareTransactionManager(
         @Qualifier("healthcareEntityManagerFactory") LocalContainerEntityManagerFactoryBean healthcareEntityManagerFactory) {
     return new JpaTransactionManager(healthcareEntityManagerFactory.getObject());
 }
}
