package com.microservices.demo.elastic.query.service.security;

import static com.microservices.demo.elastic.query.service.constants.Constants.NA;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TwitterQueryUserJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";
  private static final String SCOPE_CLAIM = "scope";
  private static final String USERNAME_CLAIM = "preferred_username";
  private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
  private static final String DEFAULT_SCOPE_PREFIX = "SCOPE_";
  private static final String SCOPE_SEPERATOR = " ";

  private final TwitterQueryUserSecurityDetailsService twitterQueryUserSecurityDetailsService;

  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    Collection<GrantedAuthority>  authorityFromJwt = getGrantedAuthorityFromJwt(jwt);
    return Optional.ofNullable(twitterQueryUserSecurityDetailsService.loadUserByUsername(jwt.getClaimAsString(USERNAME_CLAIM)))
        .map(userDetails -> {
          ((TwitterQueryUser)userDetails).setAuthorities(authorityFromJwt);
          return new UsernamePasswordAuthenticationToken(userDetails,NA,authorityFromJwt);
        }).orElseThrow(() -> new BadCredentialsException("User could not be found"));
  }

  private Collection<GrantedAuthority> getGrantedAuthorityFromJwt(Jwt jwt){
        return getCombinedAuthorites(jwt).stream()
            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  private Collection<String> getCombinedAuthorites(Jwt jwt){
    Collection<String> authorities = getRoles(jwt);
    authorities.addAll(getScopes(jwt));
    return authorities;
  }

  private Collection<String> getRoles(Jwt source){
    Object roles = ((Map<String,Object>)source.getClaims().get(REALM_ACCESS_CLAIM)).get(ROLES_CLAIM);
    if(roles instanceof Collection){
      return ((Collection<String>) roles).stream().map(s -> DEFAULT_ROLE_PREFIX + s.toUpperCase()).collect(
          Collectors.toList());
    }
    return Collections.emptyList();
  }

  private Collection<String> getScopes(Jwt source){
    Object scopes = source.getClaims().get(SCOPE_CLAIM);
    if(scopes instanceof String){
      return Arrays.stream(((String) scopes).split(SCOPE_SEPERATOR))
            .map(s -> DEFAULT_SCOPE_PREFIX+s.toUpperCase()).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }
}
