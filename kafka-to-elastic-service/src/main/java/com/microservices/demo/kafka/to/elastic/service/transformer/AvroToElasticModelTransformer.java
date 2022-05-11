package com.microservices.demo.kafka.to.elastic.service.transformer;

import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AvroToElasticModelTransformer {

  public List<TwitterIndexModel> getElasticModels(List<TwitterAvroModel> twitterAvroModels) {
    return twitterAvroModels.stream().map(this::getTwitterIndexModel)
        .collect(Collectors.toList());
  }

  private TwitterIndexModel getTwitterIndexModel(TwitterAvroModel twitterAvroModel) {
    return TwitterIndexModel.builder()
        .id(String.valueOf(twitterAvroModel.getId()))
        .userId(twitterAvroModel.getUserId())
        .text(twitterAvroModel.getText())
        .createdAt(
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(twitterAvroModel.getCreatedAt()),
                ZoneId.systemDefault())).build();
  }

}
