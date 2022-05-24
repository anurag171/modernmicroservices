package com.microservices.demo.test.rest.client;


import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableOAuth2Client
@Slf4j
public class TestRestClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestRestClientApplication.class,args);
  }

  @Component
  class TestRestClient implements CommandLineRunner{

    @Autowired
    private OAuth2RestOperations restTemplate;

    @Override
    public void run(String... args) throws Exception {
      log.info("Access Token {} ",String.valueOf(restTemplate.getAccessToken()));
    }

    @Bean
    protected OAuth2ProtectedResourceDetails resource() {
      ResourceOwnerPasswordResourceDetails resource;
      resource = new ResourceOwnerPasswordResourceDetails();

      List<String> scopes = new ArrayList<>(2);
      scopes.add("app_admin_role");
      scopes.add("app_user_role");
      resource.setAccessTokenUri("http://127.0.0.1:7069/auth/realms/microservices-realm/protocol/openid-connect/token");

      resource.setClientId("elastic-query-web-client");
      resource.setClientSecret("dcda0e0c-2a63-497e-a0d7-efbd189ffa56");
      resource.setGrantType("password");
      resource.setScope(scopes);
      resource.setUsername("app_user");
      resource.setPassword("app_user!");
      return resource;
    }

    @Bean
    public OAuth2RestOperations restTemplate() {
      AccessTokenRequest atr = new DefaultAccessTokenRequest();
      return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
    }
  }

}
