package com.microservices.demo.kafka.admin.client;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.RetryConfigData;
import com.microservices.demo.kafka.admin.exception.KafkaClientException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkAdminClient {

  private final KafkaConfigData kafkaConfigData;
  private final AdminClient adminClient;
  private final RetryConfigData retryConfigData;
  private final RetryTemplate retryTemplate;
  private final WebClient webClient;

  public void createTopic() {
    try {
      CreateTopicsResult createTopicsResult = retryTemplate.execute(this::doCreateTopic);
    } catch (Exception t) {
      throw new KafkaClientException("Maximum no of retries for creating kaka template reached ",
          t);
    }
    checkTopicsCreated();
  }

  private CreateTopicsResult doCreateTopic(RetryContext retryContext) {

    List<String> listOfTopic = kafkaConfigData.getTopicNamesToCreate();
    List<NewTopic> topicNames = listOfTopic.stream().map(s -> new NewTopic(
            s.trim(),
            kafkaConfigData.getNumOfPartitions(),
            kafkaConfigData.getReplicationFactor()))
        .collect(Collectors.toList());

    return adminClient.createTopics(topicNames);
  }

  private Collection<TopicListing> getTopics() {
    Collection<TopicListing> topicListings;
    try {
      topicListings = retryTemplate.execute(this::doGetTopics);
    } catch (Exception t) {
      throw new KafkaClientException("Maximum no of retries for creating kaka template reached ",
          t);
    }
    return topicListings;
  }

  private Collection<TopicListing> doGetTopics(RetryContext retryContext)
      throws ExecutionException, InterruptedException {
      return  adminClient.listTopics().listings().get();
  }

  public void checkTopicsCreated() {
    Collection<TopicListing> topics = getTopics();
    int retryCount = 1;
    Integer maxRetry = retryConfigData.getMaxAttempts();
    int multiplier = retryConfigData.getMultiplier().intValue();
    Long sleepTimeMs = retryConfigData.getSleepTimeMs();
    for (String topic : kafkaConfigData.getTopicNamesToCreate()) {
      while (!isTopicCreated(topics, topic)) {
        checkMaxRetry(retryCount++, maxRetry);
        sleep(sleepTimeMs);
        sleepTimeMs *= multiplier;
        topics = getTopics();
      }
    }
  }

  private void sleep(Long sleepTimeMs) {
    try {
      Thread.sleep(sleepTimeMs);
    } catch (InterruptedException e) {
      throw new KafkaClientException("Error while sleeping for waiting new created topics!!");
    }
  }

  private void checkMaxRetry(int retry, Integer maxRetry) {
    if (retry > maxRetry) {
      throw new KafkaClientException("Reached max number of retry for reading kafka topic(s)!");
    }
  }

  private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
    if (topics == null) {
      return false;
    }
    return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
  }

  public void checkSchemaRegistry() {
    int retryCount = 1;
    Integer maxRetry = retryConfigData.getMaxAttempts();
    int multiplier = retryConfigData.getMultiplier().intValue();
    Long sleepTimeMs = retryConfigData.getSleepTimeMs();
    while (!getSchemaRegistryStatus().is2xxSuccessful()) {
      checkMaxRetry(retryCount++, maxRetry);
      sleep(sleepTimeMs);
      sleepTimeMs *= multiplier;
    }
  }

  private HttpStatus getSchemaRegistryStatus() {
    try {
      return webClient
          .method(HttpMethod.GET)
          .uri(kafkaConfigData.getSchemaRegistryUrl())
          .exchange()
          .map(ClientResponse::statusCode)
          .block();
    } catch (Exception e) {
      return HttpStatus.SERVICE_UNAVAILABLE;
    }
  }
}
