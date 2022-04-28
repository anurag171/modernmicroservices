package com.microservices.demo.kafka.admin.config;

import com.microservices.demo.config.KafkaConfigData;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
@RequiredArgsConstructor
public class KafkaAdminConfig {
  private final KafkaConfigData kafkaConfigData;

  @Bean
  public AdminClient adminClient(){
    return AdminClient.create(
        Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,kafkaConfigData.getBootstrapServers()));
  }
}
