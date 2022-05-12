package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.common.util.CollectionUtil;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.repository.TwitterElasticSearchQueryRepository;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {

  private final TwitterElasticSearchQueryRepository twitterElasticSearchQueryRepository;

  @Override
  public TwitterIndexModel getIndexModelById(String id) {
    Optional<TwitterIndexModel> twitterIndexModelOptional =  twitterElasticSearchQueryRepository
                                                                .findById(id);
    if(twitterIndexModelOptional.isEmpty()){
      throw  new ElasticQueryClientException("No document found for id ="+id);
    }
    return twitterIndexModelOptional.get();
  }

  @Override
  public List<TwitterIndexModel> getIndexModelByText(String text) {
    List<TwitterIndexModel> twitterIndexModelList = twitterElasticSearchQueryRepository.findByText(text);
    if(twitterIndexModelList.isEmpty()){
      throw  new ElasticQueryClientException("No document found for text ="+text);
    }
    return twitterIndexModelList;
  }

  @Override
  public List<TwitterIndexModel> getAllIndexModels() {
    return CollectionUtil.getInstance().getListFromIterable(twitterElasticSearchQueryRepository.findAll());
  }
}
