package com.microservices.demo.elastic.query.client.service;

import com.microservices.demo.elastic.model.IndexModel;
import java.util.List;

public interface ElasticQueryClient<T extends IndexModel> {

  T getIndexModelById(String id);

  List<T> getIndexModelByText(String text);

  List<T> getAllIndexModels();

  String deleteAll(String index);
}
