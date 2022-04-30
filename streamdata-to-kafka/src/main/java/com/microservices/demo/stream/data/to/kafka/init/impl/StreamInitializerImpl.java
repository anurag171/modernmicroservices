package com.microservices.demo.stream.data.to.kafka.init.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.stream.data.to.kafka.init.StreamInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StreamInitializerImpl implements StreamInitializer {

  private final KafkaConfigData kafkaConfigData;
  private final KafkaAdminClient kafkaAdminClient;



  @Override
  public void init() {
    kafkaAdminClient.createTopic();
    kafkaAdminClient.checkSchemaRegistry();
    log.info("Topics [{}] ready",kafkaConfigData.getTopicNamesToCreate());
  }
}
