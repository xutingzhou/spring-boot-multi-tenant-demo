package com.example.multitenancy.tenant.config;

import com.example.multitenancy.master.model.MasterTenant;
import com.example.multitenancy.master.repository.MasterTenantRepository;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Xutingzhou
 */
@Configuration
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final MasterTenantRepository masterTenantRepository;

    public DataSourceBasedMultiTenantConnectionProviderImpl(MasterTenantRepository masterTenantRepository) {
        this.masterTenantRepository = masterTenantRepository;
    }

    private final Map<String, DataSource> dataSources = new TreeMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        if (dataSources.isEmpty()) {
            List<MasterTenant> tenants = masterTenantRepository.findAll();
            tenants.forEach(masterTenant -> dataSources.put(masterTenant.getTenant(), wrapperDataSource(masterTenant)));
        }
        return dataSources.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        if (!dataSources.containsKey(tenantIdentifier)) {
            List<MasterTenant> tenants = masterTenantRepository.findAll();
            tenants.forEach(masterTenant -> dataSources.put(masterTenant.getTenant(), wrapperDataSource(masterTenant)));
        }
        return dataSources.get(tenantIdentifier);
    }

    public DataSource wrapperDataSource(MasterTenant masterTenant) {
        return DataSourceBuilder
                .create()
                .url(masterTenant.getUrl())
                .username(masterTenant.getUsername())
                .password(masterTenant.getPassword())
                .driverClassName("com.mysql.cj.jdbc.Driver").build();
    }

}
