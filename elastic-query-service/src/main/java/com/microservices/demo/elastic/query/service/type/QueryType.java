package com.microservices.demo.elastic.query.service.type;


public enum QueryType {

  KAFKA_STATE_STORE("KAFKA_STATE_STORE"),ANALYTICS_DATABASE("ANALYTICS_DATABASE");

  private final String type;

  QueryType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
