package com.microservices.demo.elastic.query.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootTest
class ElasticQueryServiceApplicationTest {

  @Autowired
  Environment environment;

  @Test
  void contextLoads() {

    Assertions.assertNotNull(environment);
  }
}
