package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import com.microservices.demo.config.ElasticQueryServiceConfigData.Query;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.error.ElasticQueryServiceException;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceAnalysticsResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceWordCountModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryResponseModelAssembler;
import com.microservices.demo.elastic.query.service.type.QueryType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterElasticQueryService implements ElasticQueryService {

  private final ElasticQueryResponseModelAssembler responseModelTransformer;
  private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
  private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;
  @Qualifier("webClientBuilder")
  private final WebClient.Builder webClientBuilder;

  @Override
  public ElasticQueryServiceResponseModel getDocumentById(String id) {
    return responseModelTransformer.toModel(elasticQueryClient.getIndexModelById(id));
  }

  @Override
  public ElasticQueryServiceAnalysticsResponseModel getDocumentByText(String text,String accessToken) {
    List<ElasticQueryServiceResponseModel> elasticQueryServiceResponseModelList  =
        responseModelTransformer.toModels(elasticQueryClient.getIndexModelByText(text));
    return ElasticQueryServiceAnalysticsResponseModel
        .builder()
        .queryResponseModel(elasticQueryServiceResponseModelList)
        .wordCount(getWordCount(text,accessToken))
        .build();
  }

  private Long getWordCount(String text, String accessToken) {
    if(QueryType.KAFKA_STATE_STORE.getType().equals(elasticQueryServiceConfigData.getWebClient().getQueryType())){
      return getWordCountFromKafkaStateStore(text,accessToken).getWordCount();
    }
    return 0L;
  }

  private ElasticQueryServiceWordCountModel getWordCountFromKafkaStateStore(String text, String accessToken) {
    ElasticQueryServiceConfigData.Query  queryFromKafkaStateStore =
        elasticQueryServiceConfigData.getQueryFromKafkaStateStore();
    return retrieveResponseModel(text,accessToken,queryFromKafkaStateStore);
  }

  private ElasticQueryServiceWordCountModel retrieveResponseModel(String text, String accessToken, Query queryFromKafkaStateStore) {
    return webClientBuilder
        .build()
        .method(HttpMethod.valueOf(queryFromKafkaStateStore.getMethod()))
        .uri(queryFromKafkaStateStore.getUri(),uriBuilder -> uriBuilder.build(text))
        .accept(MediaType.valueOf(queryFromKafkaStateStore.getAccept()))
        .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
        .retrieve()
        .onStatus(httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
            clientResponse -> Mono.just(new BadCredentialsException("Not Authenticated")))
        .onStatus(HttpStatus::is4xxClientError,
            clientResponse -> Mono.just(new ElasticQueryServiceException(clientResponse.statusCode().getReasonPhrase())))
        .onStatus(HttpStatus::is5xxServerError,
            clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())))
        .bodyToMono(ElasticQueryServiceWordCountModel.class)
        .log()
        .block();
  }

  @Override
  public List<ElasticQueryServiceResponseModel> getAllDocuments() {
    return responseModelTransformer.toModels(elasticQueryClient.getAllIndexModels());
  }

  @Override
  public String deleteAll(String index) {
    return elasticQueryClient.deleteAll(index);
  }
}
