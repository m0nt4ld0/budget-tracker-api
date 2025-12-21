package com.mmontaldo.budget_tracker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.audit")
@Getter
@Setter
public class AuditConfig {
    private Boolean enabled;
    private String defaultUser;
}
