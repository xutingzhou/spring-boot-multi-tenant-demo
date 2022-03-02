package com.example.multitenancy.tenant.config;

import com.example.multitenancy.master.config.TenantContextHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        final String DEFAULT_TENANT_ID = "tenant_1";
        String currentTenantId = TenantContextHolder.getTenantId();
        return (currentTenantId != null) ? currentTenantId : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}