package com.lms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "db")
public class DatabaseProperties {
    private String url;
    private String username;
    private String password;

    // Getters and setters for the fields
}
