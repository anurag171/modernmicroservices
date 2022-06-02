package com.microservices.demo.elastic.query.service.business;

import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceAnalysticsResponseModel;
import java.util.List;

public interface ElasticQueryService {

  ElasticQueryServiceResponseModel getDocumentById(String id);
  ElasticQueryServiceAnalysticsResponseModel getDocumentByText(String text,String accessToken);
  List<ElasticQueryServiceResponseModel> getAllDocuments();
  String deleteAll(String index);
}
