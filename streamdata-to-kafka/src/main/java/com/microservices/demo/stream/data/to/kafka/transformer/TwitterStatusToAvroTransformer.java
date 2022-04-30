package com.microservices.demo.stream.data.to.kafka.transformer;

import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
public class TwitterStatusToAvroTransformer {

  public TwitterAvroModel getTwitterAvroModelFromStatus(Status status){
    return TwitterAvroModel.newBuilder()
        .setId(status.getId())
        .setText(status.getText())
        .setCreatedAt(status.getCreatedAt().getTime())
        .setUserId(status.getUser().getId())
        .build();
  }
}
