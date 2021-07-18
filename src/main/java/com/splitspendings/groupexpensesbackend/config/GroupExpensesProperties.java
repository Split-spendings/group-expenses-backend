package com.splitspendings.groupexpensesbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "groupexpenses")
@Data
public class GroupExpensesProperties {
    private String apiUrl;
}
