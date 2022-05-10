package com.microservices.demo.kafka.producer.service.impl;

import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.producer.service.KafkaProducerService;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService<Long, TwitterAvroModel> {

  private final KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

  @Override
  public void send(String topic, Long key, TwitterAvroModel message) {
    log.info("Sending message [{}] to topic [{}]", message, topic);
    ListenableFuture<SendResult<Long, TwitterAvroModel>> listenableFuture = kafkaTemplate.send(
        topic, key, message);
    addCallback(topic, message, listenableFuture);
  }

  @PreDestroy
  public void close(){
    if(kafkaTemplate != null){
      log.info("Closing kafka producer");
      kafkaTemplate.destroy();
    }
  }

  private void addCallback(String topic, TwitterAvroModel message,
      ListenableFuture<SendResult<Long, TwitterAvroModel>> listenableFuture) {
    listenableFuture.addCallback(
        new ListenableFutureCallback<SendResult<Long, TwitterAvroModel>>() {
          @Override
          public void onFailure(Throwable ex) {
            log.error("Unable to send message {} to topic {} ", message.toString(), topic);
          }

          @Override
          public void onSuccess(SendResult<Long, TwitterAvroModel> result) {
            RecordMetadata recordMetadata = result.getRecordMetadata();
            log.info("Got new metadata at time {}. Topic {} , partition {} , offset {} ,"
                    + " timestamp {}  ", System.nanoTime(), recordMetadata.topic(),
                recordMetadata.partition()
                , recordMetadata.offset(), recordMetadata.timestamp());
          }
        });
  }
}
