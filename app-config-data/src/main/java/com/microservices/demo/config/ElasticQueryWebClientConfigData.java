package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic-query-web-client")
public class ElasticQueryWebClientConfigData {
  private WebClient webClient;

  @Data
  public static class WebClient{
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private int writeTimeoutMs;
    private int maxInMemorySize;
    private String contentType;
    private String acceptType;
    private String baseUrl;
  }
}
