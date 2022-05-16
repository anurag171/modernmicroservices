package com.microservices.demo.reactive.elastic.query.service.business;

import com.microservices.demo.elastic.model.IndexModel;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;

import reactor.core.publisher.Flux;

public interface ReactiveElasticQueryClient<T extends IndexModel> {

    Flux<TwitterIndexModel> getIndexModelByText(String text);
}
