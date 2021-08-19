package com.example.multitenancy.config;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Map;

public class MyDataSourceMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    @Autowired
    private Map<String, DataSource> dataSourcesDemo;

    @Override
    protected DataSource selectAnyDataSource() {
        return this.dataSourcesDemo.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return this.dataSourcesDemo.get(tenantIdentifier);
    }
}