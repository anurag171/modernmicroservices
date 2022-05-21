package com.microservices.demo.elastic.query.service.dataacess.entity;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "user_permissions",schema = "public")
@Data
public class UserPermission {

  @NotNull
  @Id
  private UUID id;

  @NotNull
  private String username;

  @NotNull
  private String documentId;

  @NotNull
  private String permissionType;
}
