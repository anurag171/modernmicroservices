package com.microservices.demo.elastic.index.client.repository;

import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterElasticSearchIndexRepository
    extends ElasticsearchRepository<TwitterIndexModel,String> {

}
