package com.microservices.demo.elastic.query.web.client.config;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;

@Configuration
@Data
@Primary
public class ElasticQueryServiceInstanceListSupplierConfig implements ServiceInstanceListSupplier {

  private final ElasticQueryWebClientConfigData.WebClient webClientConfig;

  @Override
  public String getServiceId() {
    return webClientConfig.getServiceId();
  }

  /**
   * Gets a result.
   *
   * @return a result
   */
  @Override
  public Flux<List<ServiceInstance>> get() {
    return Flux.just(webClientConfig.getInstances().stream()
        .map(instance -> new DefaultServiceInstance(instance.getId(),
                                                    getServiceId(),
                                                    instance.getHost(),
            instance.getPort(), false)).collect(Collectors.toList()));
  }
}
