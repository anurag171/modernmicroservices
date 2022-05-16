package com.microservices.demo.reactive.elastic.query.web.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
public class ReactiveElasticQueryWebClientApplicationTests {

    @Autowired
    Environment environment;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(environment);
    }

}
