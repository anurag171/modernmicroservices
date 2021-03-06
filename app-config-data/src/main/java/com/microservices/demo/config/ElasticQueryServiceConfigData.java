package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic-query-service")
public class ElasticQueryServiceConfigData {
    private String version;
    private Long backPressureDelayMs;
    private String customAudience;
    private WebClient webClient;
    private Query queryFromKafkaStateStore;

    @Data
    public static class WebClient{
        private int connectTimeoutMs;
        private int readTimeoutMs;
        private int writeTimeoutMs;
        private int maxInMemorySize;
        private String contentType;
        private String acceptType;
        private String queryType;
    }

    @Data
    public static class Query{
        private String method;
        private String accept;
        private String uri;
    }
}
