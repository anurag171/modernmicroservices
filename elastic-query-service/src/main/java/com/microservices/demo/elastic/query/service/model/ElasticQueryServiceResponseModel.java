package com.microservices.demo.elastic.query.service.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQueryServiceResponseModel
    extends RepresentationModel<ElasticQueryServiceResponseModel> {
  private String id;
  private String text;
  private Long userId;
  private LocalDateTime createdAt;
}
