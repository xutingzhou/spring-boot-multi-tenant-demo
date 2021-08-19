package com.example.multitenancy.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Xutingzhou
 */
@Aspect
@Configuration
public class SchedulerConfig {

    @Autowired
    private Map<String, DataSource> dataSourcesDemo;

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void doIt(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        for ( String tenant : dataSourcesDemo.keySet()) {
            MyTenantContext.setTenantId(tenant);
            proceedingJoinPoint.proceed();
            MyTenantContext.clear();
        }
    }

}
