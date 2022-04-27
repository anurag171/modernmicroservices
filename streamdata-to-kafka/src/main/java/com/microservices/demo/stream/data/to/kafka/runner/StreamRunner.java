package com.microservices.demo.stream.data.to.kafka.runner;

import twitter4j.TwitterException;

public interface StreamRunner {

  void start() throws TwitterException;

}
