package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter-oauth")
public class TwitterConfigData {
  private String consumerKey;
  private String consumerSecret;
  private String accessToken;
  private String accessTokenSecret;
}
