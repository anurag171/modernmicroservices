package com.microservices.demo.elastic.query.web.client.service.impl;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.elastic.query.web.client.exception.ElasticQueryWebClientException;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.elastic.query.web.client.service.ElasticQueryWebClient;
import java.lang.reflect.Type;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterElasticQueryWebClient implements ElasticQueryWebClient {

  private final ElasticQueryWebClientConfigData elasticQueryWebClientConfigData;
  @Qualifier("webClientBuilder")
  private final WebClient.Builder builder;

  @Override
  public List<ElasticQueryWebClientResponseModel> getDataByText(
      ElasticQueryWebClientRequestModel requestModel) {
      return getWebClient(requestModel)
          .bodyToFlux(ElasticQueryWebClientResponseModel.class)
          .collectList()
          .block();
  }

  private WebClient.ResponseSpec getWebClient(ElasticQueryWebClientRequestModel requestModel){
    return builder.build()
        .method(HttpMethod.valueOf(elasticQueryWebClientConfigData.getQueryByText().getMethod()))
        .uri(elasticQueryWebClientConfigData.getQueryByText().getUri())
        .accept(MediaType.valueOf("application/vnd.api.v1+json"))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromPublisher(Mono.just(requestModel),createParametrizedTypeReference()))
        .retrieve()
        .onStatus(httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
              clientResponse -> Mono.just(new BadCredentialsException("Not Authenticated")))
        .onStatus(HttpStatus::is4xxClientError,
            clientResponse -> Mono.just(new ElasticQueryWebClientException(clientResponse.statusCode().getReasonPhrase())))
        .onStatus(HttpStatus::is5xxServerError,
            clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())));
  }


  private <T> ParameterizedTypeReference<T>  createParametrizedTypeReference() {
    return new ParameterizedTypeReference<>() {
    };
  }


}
