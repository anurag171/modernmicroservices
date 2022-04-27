package com.stream.data.to.kafka.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "stream-data-to-kafka-service")
public class StreamDataToKafkaConfig {

  private List<String> twitterKeywords;
  private String welcomeMessage;

}
