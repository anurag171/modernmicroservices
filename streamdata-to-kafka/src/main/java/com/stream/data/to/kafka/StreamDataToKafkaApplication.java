package com.stream.data.to.kafka;

import com.stream.data.to.kafka.config.StreamDataToKafkaConfig;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class StreamDataToKafkaApplication implements CommandLineRunner {

  private final StreamDataToKafkaConfig streamDataToKafkaConfig;

  public static void main(String[] args) {
    SpringApplication.run(StreamDataToKafkaApplication.class,args);
  }


  @Override
  public void run(String... args) throws Exception {
    log.info("App started");
    log.info(Arrays.toString(streamDataToKafkaConfig.getTwitterKeywords().toArray(new String[]{})));
    log.info(streamDataToKafkaConfig.getWelcomeMessage());
  }
}
