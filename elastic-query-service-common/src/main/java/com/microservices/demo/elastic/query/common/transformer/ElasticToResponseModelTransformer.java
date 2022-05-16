package com.microservices.demo.elastic.query.common.transformer;

import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel twitterIndexModel){
      return getElasticQueryServiceResponseModel(twitterIndexModel);
    }

  public List<ElasticQueryServiceResponseModel> getResponseModelList(
      List<TwitterIndexModel> twitterIndexModelList){

    return  twitterIndexModelList.stream().map(this::getElasticQueryServiceResponseModel).collect(
        Collectors.toList());
  }

  private ElasticQueryServiceResponseModel getElasticQueryServiceResponseModel(TwitterIndexModel twitterIndexModel){
    return  ElasticQueryServiceResponseModel.builder()
        .id(twitterIndexModel.getId())
        .text(twitterIndexModel.getText())
        .createdAt(twitterIndexModel.getCreatedAt())
        .userId(twitterIndexModel.getUserId()).build();
  }

}
