package com.microservices.demo.elastic.query.web.client.config;

import com.microservices.demo.config.UserConfigData;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

//@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  //private final UserConfigData userConfigData;
  private final ClientRegistrationRepository clientRegistrationRepository;

  private final String ROLE_PREFIX = "ROLE_";
  private final String GROUPS = "groups";


  @Value("${security.logout-success-uri}")
  private String logoutSuccessUri;

  OidcClientInitiatedLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler(){
    OidcClientInitiatedLogoutSuccessHandler successHandler =
    new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
    successHandler.setPostLogoutRedirectUri(logoutSuccessUri);
    return successHandler;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .anyRequest()
        .fullyAuthenticated()
        .and()
        .logout()
        .logoutSuccessHandler(oidcClientInitiatedLogoutSuccessHandler())
        .and()
        .oauth2Client()
        .and()
        .oauth2Login()
        .userInfoEndpoint()
        .userAuthoritiesMapper(userAuthoritiesMapper());
  }

  private GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities)->{
      Set<GrantedAuthority> authoritySet = new HashSet<>();

      authoritySet.forEach(grantedAuthority -> {
        if(grantedAuthority instanceof OidcUserAuthority){
          OidcUserAuthority oidcUserAuthority= (OidcUserAuthority) grantedAuthority;
          OidcIdToken oidcIdToken= oidcUserAuthority.getIdToken();
          log.info("Username from id token [{}]",oidcIdToken.getPreferredUsername());
          OidcUserInfo oidcUserInfo = oidcUserAuthority.getUserInfo();
          List<SimpleGrantedAuthority> simpleGrantedAuthorities =
              oidcUserInfo.getClaimAsStringList(GROUPS).stream()
                  .map(s -> new SimpleGrantedAuthority(ROLE_PREFIX+s.toUpperCase()))
                  .collect(Collectors.toList());
          authoritySet.addAll(simpleGrantedAuthorities);
        }
      });
      return authoritySet;
    };
  }



  /*@Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .inMemoryAuthentication()
        .withUser(userConfigData.getUsername())
        .password(passwordEncoder().encode(userConfigData.getPassword()))
        .roles(userConfigData.getRoles());
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }*/
}
