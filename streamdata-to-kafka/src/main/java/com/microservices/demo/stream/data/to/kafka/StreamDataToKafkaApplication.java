package com.microservices.demo.stream.data.to.kafka;

import com.microservices.demo.config.StreamDataToKafkaConfig;
import com.microservices.demo.stream.data.to.kafka.init.StreamInitializer;
import com.microservices.demo.stream.data.to.kafka.runner.StreamRunner;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.microservices.demo")
public class StreamDataToKafkaApplication implements CommandLineRunner {

  private final StreamDataToKafkaConfig streamDataToKafkaConfig;
  private final StreamRunner statusRunner;
  private final StreamInitializer streamInitializer;

  public static void main(String[] args) {
    SpringApplication.run(StreamDataToKafkaApplication.class,args);
  }


  @Override
  public void run(String... args) throws Exception {
    log.info("App started");
    log.info(Arrays.toString(streamDataToKafkaConfig.getTwitterKeywords().toArray(new String[]{})));
    log.info(streamDataToKafkaConfig.getWelcomeMessage());
    streamInitializer.init();
    statusRunner.start();
  }
}
