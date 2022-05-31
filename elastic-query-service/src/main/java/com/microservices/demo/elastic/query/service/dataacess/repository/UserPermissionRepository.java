package com.microservices.demo.elastic.query.service.dataacess.repository;

import com.microservices.demo.elastic.query.service.dataacess.entity.UserPermission;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPermissionRepository extends JpaRepository<UserPermission, UUID> {

  @Query(nativeQuery = true,value = "SELECT p.user_permission_id as id,u.username,d.document_id,p.permission_type "
  + " FROM user_permissions p ,users u , documents d WHERE u.id = p.user_id "
      + " and p.document_id= d.id and u.username=:username")
  Optional<List<UserPermission>> findPermissionsByUsername(@Param("username") String username);

}
