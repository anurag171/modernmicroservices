package com.microservices.demo.elastic.query.client.exception;

public class ElasticQueryClientException extends
    RuntimeException {

  public ElasticQueryClientException(String message) {
    super(message);
  }

  public ElasticQueryClientException(String message,Throwable ex) {
    super(message,ex);
  }

  public ElasticQueryClientException() {
    super();
  }
}