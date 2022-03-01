package com.example.multitenancy.master.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Xutingzhou
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("master.datasource")
public class MasterDatabaseProperties {

    private String url;
    private String password;
    private String username;
    private String driverClassName;

}
