package com.microservices.demo.elastic.config;

import com.microservices.demo.config.ElasticConfigData;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = "com.microservices.demo.elastic")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

  private final ElasticConfigData elasticConfigData;

  @Bean
  @Override
  public RestHighLevelClient elasticsearchClient() {
    UriComponents uriComponents = UriComponentsBuilder
        .fromHttpUrl(elasticConfigData.getConnectionUrl())
        .build();

    return new RestHighLevelClient(
        RestClient.builder(new HttpHost(
            Objects.requireNonNull(uriComponents.getHost()),
            uriComponents.getPort(),
            uriComponents.getScheme()
        )).setRequestConfigCallback(
            requestConfigBuilder -> requestConfigBuilder
                .setConnectTimeout(elasticConfigData
                    .getConnectTimeoutMs())
                .setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
        )
    );
  }


  @Bean
  public ElasticsearchOperations elasticsearchTemplate() {
    return new ElasticsearchRestTemplate(elasticsearchClient());
  }
}
