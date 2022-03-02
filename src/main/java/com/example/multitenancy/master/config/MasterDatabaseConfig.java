package com.example.multitenancy.master.config;

import com.example.multitenancy.master.model.MasterTenant;
import com.example.multitenancy.master.repository.MasterTenantRepository;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Xutingzhou
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({JpaProperties.class})
@EnableJpaRepositories(
        basePackages = {
                "com.example.multitenancy.master.model",
                "com.example.multitenancy.master.repository",
        },
        entityManagerFactoryRef = "masterEntityManagerFactory",
        transactionManagerRef = "masterTransactionManager"
)
public class MasterDatabaseConfig {

    private final JpaProperties jpaProperties;
    private final MasterDatabaseProperties masterDatabaseProperties;

    public MasterDatabaseConfig(JpaProperties jpaProperties, MasterDatabaseProperties masterDatabaseProperties) {
        this.jpaProperties = jpaProperties;
        this.masterDatabaseProperties = masterDatabaseProperties;
    }

    @Bean(name = "masterDatasource")
    public DataSource masterDatasource() {
        return DataSourceBuilder
                .create()
                .url(masterDatabaseProperties.getUrl())
                .username(masterDatabaseProperties.getUsername())
                .password(masterDatabaseProperties.getPassword())
                .driverClassName(masterDatabaseProperties.getDriverClassName()).build();
    }

    @Primary
    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(masterDatasource());
        factoryBean.setPackagesToScan(
                MasterTenant.class.getPackage().getName(), MasterTenantRepository.class.getPackage().getName());
        factoryBean.setPersistenceUnitName("master-database-persistence-unit");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> hibernateProps = new LinkedHashMap<>(jpaProperties.getProperties());
        hibernateProps.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
        hibernateProps.put(Environment.HBM2DDL_AUTO, "update");
        hibernateProps.put(Environment.PHYSICAL_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        hibernateProps.put(Environment.IMPLICIT_NAMING_STRATEGY, "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        hibernateProps.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);
        factoryBean.setJpaPropertyMap(hibernateProps);

        return factoryBean;
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

}
