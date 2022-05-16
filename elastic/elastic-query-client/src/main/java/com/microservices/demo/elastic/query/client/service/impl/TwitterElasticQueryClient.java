package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.config.ElasticQueryConfigData;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.client.util.ElasticQueryUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class TwitterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {

  private final ElasticConfigData elasticConfigData;
  private final ElasticQueryConfigData elasticQueryConfigData;
  private final ElasticsearchOperations elasticsearchOperations;
  private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;

  @Override
  public TwitterIndexModel getIndexModelById(String id) {
    Query query = elasticQueryUtil.getSearchQueryById(id);
    SearchHit<TwitterIndexModel>  modelSearchHit = elasticsearchOperations.searchOne(query,TwitterIndexModel.class,
                                          IndexCoordinates.of(elasticConfigData.getIndexName()));
    if(modelSearchHit==null){
      throw  new ElasticQueryClientException("No document found for id ="+id);
    }
    return modelSearchHit.getContent();
  }

  @Override
  public List<TwitterIndexModel> getIndexModelByText(String text) {
    Query query =  elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(),text);
    return search(query, "No document found for text ="+text);
  }

  @Override
  public List<TwitterIndexModel> getAllIndexModels() {
    Query query =  elasticQueryUtil.getSearchQueryForAll();
    return search(query, "No document found ");
  }

  @Override
  public String deleteAll(String index) {
    elasticsearchOperations.delete(index,TwitterIndexModel.class);
    return "done";
  }

  private List<TwitterIndexModel> search(Query query, String exceptionMessage) {
    SearchHits<TwitterIndexModel> modelSearchHits = elasticsearchOperations.search(query,TwitterIndexModel.class,IndexCoordinates.of(elasticConfigData.getIndexName()));
    if(modelSearchHits.isEmpty()){
      throw  new ElasticQueryClientException(exceptionMessage);
    }
    return modelSearchHits.stream().map(SearchHit::getContent).collect(
        Collectors.toList());
  }
}
