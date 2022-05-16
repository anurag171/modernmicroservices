package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryResponseModelAssembler;;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterElasticQueryService implements ElasticQueryService {

  private final ElasticQueryResponseModelAssembler responseModelTransformer;
  private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;

  @Override
  public ElasticQueryServiceResponseModel getDocumentById(String id) {
    return responseModelTransformer.toModel(elasticQueryClient.getIndexModelById(id));
  }

  @Override
  public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
    return responseModelTransformer.toModels(elasticQueryClient.getIndexModelByText(text));
  }

  @Override
  public List<ElasticQueryServiceResponseModel> getAllDocuments() {
    return responseModelTransformer.toModels(elasticQueryClient.getAllIndexModels());
  }

  @Override
  public String deleteAll(String index) {
    return elasticQueryClient.deleteAll(index);
  }
}
