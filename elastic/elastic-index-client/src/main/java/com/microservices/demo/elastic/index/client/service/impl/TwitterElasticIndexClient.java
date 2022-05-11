package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.index.client.util.ElasticIndexUtil;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterIndexModel> {

  private final ElasticConfigData elasticConfigData;
  private final ElasticsearchOperations elasticsearchOperations;
  private final ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil;

  @Override
  public List<String> save(List<TwitterIndexModel> documents) {
    List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);
    List<String> documentIds = elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(elasticConfigData.getIndexName()));
    log.info("List of document ids [{}]",documentIds.toArray(new String[]{}));
    return documentIds;
  }
}
