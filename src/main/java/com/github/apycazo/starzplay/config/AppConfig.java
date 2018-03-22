package com.github.apycazo.starzplay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig
{
    private String catalogURL;
    private List<String> allowedFilters;
    private String mapping;

}
