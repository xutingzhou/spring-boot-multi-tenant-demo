package com.example.multitenancy.master.config;

import com.example.multitenancy.master.model.MasterTenant;
import com.example.multitenancy.master.repository.MasterTenantRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Xutingzhou
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "com.example.multitenancy.master.model",
                "com.example.multitenancy.master.repository",
        },
        entityManagerFactoryRef = "masterEntityManagerFactory",
        transactionManagerRef = "masterTransactionManager"
)
public class MasterDatabaseConfig {

    private final MasterDatabaseProperties masterDatabaseProperties;

    public MasterDatabaseConfig(MasterDatabaseProperties masterDatabaseProperties) {
        this.masterDatabaseProperties = masterDatabaseProperties;
    }

    @Bean(name = "masterDatasource")
    public DataSource masterDatasource() {
        HikariDataSource datasource = new HikariDataSource();
        datasource.setUsername(masterDatabaseProperties.getUsername());
        datasource.setPassword(masterDatabaseProperties.getPassword());
        datasource.setJdbcUrl(masterDatabaseProperties.getUrl());
        datasource.setDriverClassName(masterDatabaseProperties.getDriverClassName());
        return datasource;
    }

    @Primary
    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lb = new LocalContainerEntityManagerFactoryBean();
        lb.setDataSource(masterDatasource());
        lb.setPackagesToScan(
                MasterTenant.class.getPackage().getName(), MasterTenantRepository.class.getPackage().getName());

        //Setting a name for the persistence unit as Spring sets it as 'default' if not defined.
        lb.setPersistenceUnitName("master-database-persistence-unit");

        //Setting Hibernate as the JPA provider.
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        lb.setJpaVendorAdapter(vendorAdapter);

        //Setting the hibernate properties
        lb.setJpaProperties(hibernateProperties());

        return lb;
    }

    @Bean(name = "masterTransactionManager")
    public JpaTransactionManager masterTransactionManager(@Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "update");
        return properties;
    }
}
