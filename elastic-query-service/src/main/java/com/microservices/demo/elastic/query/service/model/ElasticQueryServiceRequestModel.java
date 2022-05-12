package com.microservices.demo.elastic.query.service.model;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQueryServiceRequestModel {

  private String id;
  @NotEmpty
  private String text;
}
