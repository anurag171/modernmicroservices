package com.microservices.demo.elastic.query.service.security;

import lombok.Getter;

@Getter
public enum PermissionType {

  READ("READ"),WRITE("WRITE"),ADMIN("ADMIN");

  private final String type;

  PermissionType(String type){
    this.type = type;
  }
}
