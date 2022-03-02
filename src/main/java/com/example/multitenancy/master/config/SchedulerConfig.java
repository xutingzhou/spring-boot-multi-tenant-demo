package com.example.multitenancy.master.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Xutingzhou
 */
@Aspect
@Configuration
public class SchedulerConfig {

    private final Map<String, DataSource> dataSourcesDemo;

    public SchedulerConfig(Map<String, DataSource> dataSourcesDemo) {
        this.dataSourcesDemo = dataSourcesDemo;
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public boolean doIt(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        for (String tenant : dataSourcesDemo.keySet()) {
            TenantContextHolder.setTenantId(tenant);
            proceedingJoinPoint.proceed();
            TenantContextHolder.clear();
        }
        return true;
    }

}
