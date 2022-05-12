package com.microservices.demo.elastic.query.service.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQueryServiceResponseModel {
  private String id;
  private String text;
  private Long userId;
  private LocalDateTime createdAt;
}
