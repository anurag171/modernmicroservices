package com.microservices.demo.elastic.query.service.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class ElasticQueryServiceResponseModelV2
    extends RepresentationModel<ElasticQueryServiceResponseModelV2> {
  private Long id;
  private String text;
  private String textV2;
  private Long userId;
  private LocalDateTime createdAt;
}
