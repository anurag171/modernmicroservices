package com.microservices.demo.config.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
@Slf4j
public class ConfigServer {

  public static void main(String[] args) {
    log.info("args lenght " + args.length);
    for(String arg:args){
      log.info("Reading arg " + arg);
    }
    SpringApplication.run(ConfigServer.class,args);
  }

}
