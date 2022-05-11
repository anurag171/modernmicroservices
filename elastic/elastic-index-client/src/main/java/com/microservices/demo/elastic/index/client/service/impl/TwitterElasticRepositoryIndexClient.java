package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.repository.TwitterElasticSearchIndexRepository;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "elastic-config.is-repository",havingValue = "true",matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {

  private final TwitterElasticSearchIndexRepository twitterElasticSearchIndexRepository;

  @Override
  public List<String> save(List<TwitterIndexModel> documents) {
    List<TwitterIndexModel> repositoryResponse=
        (List<TwitterIndexModel>) twitterElasticSearchIndexRepository.saveAll(documents);
    List<String> documentIds= repositoryResponse.stream()
        .map(TwitterIndexModel::getId).collect(Collectors.toList());
    log.info("List of document ids [{}]",documentIds.toArray(new String[]{}));
    return documentIds;
  }
}
