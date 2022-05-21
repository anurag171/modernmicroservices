package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.elastic.query.service.business.QueryUserService;
import com.microservices.demo.elastic.query.service.dataacess.entity.UserPermission;
import com.microservices.demo.elastic.query.service.dataacess.repository.UserPermissionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwitterQueryUserService implements QueryUserService {

  private final UserPermissionRepository userPermissionRepository;

  @Override
  public Optional<List<UserPermission>> findAllPermissionByUsername(String username) {
    log.info("finding user permission for username [{}]",username);
    return userPermissionRepository.findPermissionsByUsername(username);
  }
}
