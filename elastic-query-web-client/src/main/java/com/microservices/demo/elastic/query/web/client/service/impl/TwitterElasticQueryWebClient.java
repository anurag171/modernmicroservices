package com.microservices.demo.elastic.query.web.client.service.impl;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.elastic.query.web.client.service.ElasticQueryWebClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterElasticQueryWebClient implements ElasticQueryWebClient {

  private final ElasticQueryWebClientConfigData elasticQueryWebClientConfigData;
  private final WebClient.Builder builder;

  @Override
  public List<ElasticQueryWebClientResponseModel> getDataByText(
      ElasticQueryWebClientRequestModel requestModel) {
    return null;
  }
}
