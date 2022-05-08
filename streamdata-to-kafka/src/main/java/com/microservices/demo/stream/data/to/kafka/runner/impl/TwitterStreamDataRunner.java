package com.microservices.demo.stream.data.to.kafka.runner.impl;

import com.microservices.demo.config.StreamDataToKafkaConfig;
import com.microservices.demo.config.TwitterConfigData;
import com.microservices.demo.stream.data.to.kafka.listener.StreamDataStatusListener;
import com.microservices.demo.stream.data.to.kafka.runner.StreamRunner;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "stream-data-to-kafka-service.enable-mock-tweets", havingValue = "false", matchIfMissing = true)
public class TwitterStreamDataRunner implements StreamRunner {

  private final StreamDataToKafkaConfig streamDataToKafkaConfig;
  private final StreamDataStatusListener streamDataStatusListener;
  private final TwitterConfigData twitterConfigData;

  private TwitterStream twitterStream;

  @Override
  public void start() throws TwitterException {

    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
        .setOAuthConsumerKey(twitterConfigData.getConsumerKey())
        .setOAuthConsumerSecret(twitterConfigData.getConsumerSecret())
        .setOAuthAccessToken(twitterConfigData.getAccessToken())
        .setOAuthAccessTokenSecret(twitterConfigData.getAccessTokenSecret());

    Configuration configuration = cb.build();
    log.info(configuration.toString());


    twitterStream = new TwitterStreamFactory().getInstance();
    twitterStream.setOAuthConsumer(configuration.getOAuthConsumerKey(),configuration.getOAuthConsumerSecret());
    twitterStream.setOAuthAccessToken(new AccessToken(configuration.getOAuthAccessToken(),
        configuration.getOAuthAccessTokenSecret()));
    twitterStream.addListener(streamDataStatusListener);
    addFilter();

  }

  private void addFilter() {
    String[] keywords = streamDataToKafkaConfig.getTwitterKeywords().toArray(new String[]{});
    FilterQuery filterQuery = new FilterQuery(keywords);
    twitterStream.filter(filterQuery);
    log.info("Started filtering stream for keywords {}",streamDataToKafkaConfig.getTwitterKeywords());
  }

  @PreDestroy
  public void shutDown(){
    if(twitterStream != null){
      twitterStream.shutdown();
    }
  }
}
