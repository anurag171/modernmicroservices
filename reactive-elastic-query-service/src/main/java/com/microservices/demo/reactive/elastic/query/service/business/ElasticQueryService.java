package com.microservices.demo.reactive.elastic.query.service.business;

import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import reactor.core.publisher.Flux;

public interface ElasticQueryService {

    Flux<ElasticQueryServiceResponseModel> getDocumentByText(String text);
}
