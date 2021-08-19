package com.example.multitenancy.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class MyTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        final String DEFAULT_TENANT_ID = "tenant_1";
        String currentTenantId = MyTenantContext.getTenantId();
        return (currentTenantId != null) ? currentTenantId : DEFAULT_TENANT_ID;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}