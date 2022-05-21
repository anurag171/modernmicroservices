package com.microservices.demo.elastic.query.service.security;

import com.microservices.demo.elastic.query.service.business.QueryUserService;
import com.microservices.demo.elastic.query.service.transformer.UserPermissionToTwitterUserDetailTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitterQueryUserSecurityDetailsService implements UserDetailsService {

  private final QueryUserService queryUserService;
  private final UserPermissionToTwitterUserDetailTransformer transformer;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return queryUserService.findAllPermissionByUsername(username)
        .map(transformer::getUserDetails)
        .orElseThrow(() -> new UsernameNotFoundException("No user found for username "+username));
  }
}
