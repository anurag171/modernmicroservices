package com.microservices.demo.stream.data.to.kafka.listener;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.producer.service.KafkaProducerService;
import com.microservices.demo.stream.data.to.kafka.transformer.TwitterStatusToAvroTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
@Slf4j
@RequiredArgsConstructor
public class StreamDataStatusListener extends StatusAdapter {

  private final KafkaConfigData kafkaConfigData;
  private final KafkaProducerService<Long, TwitterAvroModel> kafkaProducerService;
  private final TwitterStatusToAvroTransformer twitterStatusToAvroTransformer;

  @Override
  public void onStatus(Status status) {
    log.info("Twitter status with text {} send to topic {} ",status.getText(),kafkaConfigData.getTopicName());
    var twitterAvroModel = twitterStatusToAvroTransformer.getTwitterAvroModelFromStatus(status);
    kafkaProducerService.send(kafkaConfigData.getTopicName(),twitterAvroModel.getUserId(),twitterAvroModel);
  }
}
