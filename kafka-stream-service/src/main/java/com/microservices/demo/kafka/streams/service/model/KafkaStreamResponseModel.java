package com.microservices.demo.kafka.streams.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class KafkaStreamResponseModel {

  private String word;
  private Long wordCount;

}
