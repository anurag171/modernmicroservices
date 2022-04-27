package com.stream.data.to.kafka.runner.impl;

import com.stream.data.to.kafka.config.StreamDataToKafkaConfig;
import com.stream.data.to.kafka.listener.StreamDataStatusListener;
import com.stream.data.to.kafka.runner.StreamRunner;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Component
@RequiredArgsConstructor
@Slf4j
public class TwitterStreamDataRunner implements StreamRunner {

  private final StreamDataToKafkaConfig streamDataToKafkaConfig;
  private final StreamDataStatusListener streamDataStatusListener;

  private TwitterStream twitterStream;

  @Override
  public void start() throws TwitterException {

    twitterStream = new TwitterStreamFactory().getInstance();
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
