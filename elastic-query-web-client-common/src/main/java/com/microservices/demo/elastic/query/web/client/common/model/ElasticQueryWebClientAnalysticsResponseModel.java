package com.microservices.demo.elastic.query.web.client.common.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQueryWebClientAnalysticsResponseModel {

  private List<ElasticQueryWebClientResponseModel> elasticQueryWebClientResponseModelList;
  private Long wordCount;

}
