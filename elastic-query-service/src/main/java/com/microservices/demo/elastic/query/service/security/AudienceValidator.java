package com.microservices.demo.elastic.query.service.security;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@Qualifier("elastic-query-service-audience-validator")
@RequiredArgsConstructor
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

  private ElasticQueryServiceConfigData elasticQueryServiceConfigData;

  @Override
  public OAuth2TokenValidatorResult validate(Jwt token) {
    if(token.getAudience().contains(elasticQueryServiceConfigData.getCustomAudience())){
      return OAuth2TokenValidatorResult.success();
    }else {
      OAuth2Error auth2Error = new OAuth2Error("invalid-token",
          String.format("The required %s audience is missing",
              elasticQueryServiceConfigData.getCustomAudience()),"");
      return OAuth2TokenValidatorResult.failure(auth2Error);

    }
  }
}
