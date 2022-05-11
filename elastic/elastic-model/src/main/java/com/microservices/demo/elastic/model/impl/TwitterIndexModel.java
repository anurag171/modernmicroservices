package com.microservices.demo.elastic.model.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microservices.demo.elastic.model.IndexModel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.elasticsearch.cluster.metadata.Metadata.Custom;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Data
@Document(indexName = "#{elasticConfigData.indexName}")
public class TwitterIndexModel implements IndexModel {

  @JsonProperty
  private String id;

  @JsonProperty
  private Long userId;

  @JsonProperty
  private String text;

  @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
  @JsonFormat(shape = Shape.STRING,pattern = "uuuu-MM-dd'T'HH:mm:ssZZ" )
  @JsonProperty
  private LocalDateTime createdAt;
}
