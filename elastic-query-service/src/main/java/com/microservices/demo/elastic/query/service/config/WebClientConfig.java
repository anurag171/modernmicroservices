package com.microservices.demo.elastic.query.service.config;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
public class WebClientConfig {

  private final ElasticQueryServiceConfigData.WebClient elasticQueryServiceConfigData;

  public WebClientConfig(
      ElasticQueryServiceConfigData elasticQueryServiceConfigData) {
    this.elasticQueryServiceConfigData = elasticQueryServiceConfigData.getWebClient();
  }

  @LoadBalanced
  @Bean("webClientBuilder")
  public org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder() {
    return WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, elasticQueryServiceConfigData.getContentType())
        .defaultHeader(HttpHeaders.ACCEPT, elasticQueryServiceConfigData.getAcceptType())
        .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTcpClient())))
        .codecs(clientCodecConfigurer -> clientCodecConfigurer
            .defaultCodecs()
            .maxInMemorySize(elasticQueryServiceConfigData.getMaxInMemorySize()));
  }

  private TcpClient getTcpClient() {

    return TcpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
            elasticQueryServiceConfigData.getConnectTimeoutMs())
        .doOnConnected(connection -> {
          connection.addHandlerLast(
              new ReadTimeoutHandler(elasticQueryServiceConfigData.getReadTimeoutMs()));
          connection.addHandlerLast(
              new WriteTimeoutHandler(elasticQueryServiceConfigData.getWriteTimeoutMs()));
        });
  }
}
