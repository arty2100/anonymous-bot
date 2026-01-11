package com.metapraktika.anonymousbot.properties;

import com.metapraktika.anonymousbot.enums.RoleType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

    private Map<RoleType, Integer> limits;

    public Map<RoleType, Integer> getLimits() {
        return limits;
    }

    public void setLimits(Map<RoleType, Integer> limits) {
        this.limits = limits;
    }
}
