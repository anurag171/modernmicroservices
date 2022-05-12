package com.microservices.demo.elastic.query.client.repository;

import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterElasticSearchQueryRepository extends ElasticsearchRepository<TwitterIndexModel,String> {

  List<TwitterIndexModel> findByText(String text);

}
