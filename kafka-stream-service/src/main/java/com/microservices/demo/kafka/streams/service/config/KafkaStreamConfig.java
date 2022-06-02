package com.microservices.demo.kafka.streams.service.config;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaStreamsConfigData;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamConfig {
  private final KafkaStreamsConfigData kafkaStreamsConfigData;
  private final KafkaConfigData kafkaConfigData;

  @Bean
  @Qualifier("streamCongifguration")
  public Properties streamConfiguration(){
    Properties streamConfiguration = new Properties();
    streamConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG,kafkaStreamsConfigData.getApplicationID());
    streamConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConfigData.getBootstrapServers());
    streamConfiguration.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
        kafkaConfigData.getSchemaRegistryUrl());
    streamConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    streamConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass().getName());
    streamConfiguration.put(StreamsConfig.STATE_DIR_CONFIG,kafkaStreamsConfigData.getStateFileLocation());
    return streamConfiguration;
  }
}
