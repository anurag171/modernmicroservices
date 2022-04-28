package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix ="retry-config")
@Data
public class RetryConfigData {
    private Long initiaLIntervalMs;
    private Long maxIntervalMs;
    private Double multiplier;
    private Integer maxAttempts;
    private Long sleepTimeMs;
}
