package com.microservices.demo.elastic.query.service.config;

import com.microservices.demo.config.UserConfigData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserConfigData userConfigData;

  @Value("${security.paths-to-ignore}")
  private String[] pathsToIgnore;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/**").hasRole("USER")
        .and()
        .csrf().disable();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
        .ignoring()
        .antMatchers(pathsToIgnore);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .inMemoryAuthentication()
            .withUser(userConfigData.getUsername())
                .password(passwordEncoder().encode(userConfigData.getPassword()))
                    .roles(userConfigData.getRoles());
  }

  @Bean
  protected PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
}
