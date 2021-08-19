package com.example.multitenancy.config;

import com.example.multitenancy.model.User;
import com.example.multitenancy.repository.UserRepository;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Xutingzhou
 */
@Configuration
@EnableConfigurationProperties({MultiTenantProperties.class, JpaProperties.class})
@EnableJpaRepositories(basePackages = {"com.example.multitenancy"})
@EnableTransactionManagement
public class MultiTenantJpaConfiguration {

    private final JpaProperties jpaProperties;
    private final MultiTenantProperties multiTenantProperties;

    public MultiTenantJpaConfiguration(JpaProperties jpaProperties,
                                       MultiTenantProperties multiTenantProperties) {
        this.jpaProperties = jpaProperties;
        this.multiTenantProperties = multiTenantProperties;
    }

    @Bean(name = "dataSourcesDemo")
    public Map<String, DataSource> dataSourcesDemo() {
        Map<String, DataSource> result = new HashMap<>();
        for (MultiTenantProperties.DataSourceProperties dsProperties : this.multiTenantProperties.getDataSources()) {
            DataSourceBuilder factory = DataSourceBuilder
                    .create()
                    .url(dsProperties.getUrl())
                    .username(dsProperties.getUsername())
                    .password(dsProperties.getPassword())
                    .driverClassName(dsProperties.getDriverClassName());
            result.put(dsProperties.getTenantId(), factory.build());
        }
        return result;
    }

    @Bean
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new MyDataSourceMultiTenantConnectionProviderImpl();
    }

    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new MyTenantIdentifierResolverImpl();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                           CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        Map<String, Object> hibernateProps = new LinkedHashMap<>(jpaProperties.getProperties());
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
        hibernateProps.put(Environment.HBM2DDL_AUTO, "update");

        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setPackagesToScan(User.class.getPackage().getName(), UserRepository.class.getPackage().getName());
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        result.setJpaPropertyMap(hibernateProps);

        return result;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
