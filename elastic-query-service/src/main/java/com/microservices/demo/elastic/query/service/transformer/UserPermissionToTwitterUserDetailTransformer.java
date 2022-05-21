package com.microservices.demo.elastic.query.service.transformer;

import com.microservices.demo.elastic.query.service.dataacess.entity.UserPermission;
import com.microservices.demo.elastic.query.service.security.PermissionType;
import com.microservices.demo.elastic.query.service.security.TwitterQueryUser;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionToTwitterUserDetailTransformer {

  public TwitterQueryUser getUserDetails(List<UserPermission> userPermissions){
      return TwitterQueryUser.builder().username(userPermissions.get(0).getUsername())
          .permissions(userPermissions.stream().collect(
              Collectors.toMap(
                  UserPermission::getDocumentId,
                  userPermission -> PermissionType.valueOf(userPermission.getPermissionType())
              ))).build();
  }

}
