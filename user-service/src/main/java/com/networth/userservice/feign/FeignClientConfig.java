package com.networth.userservice.feign;

import org.springframework.context.annotation.Bean;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                                 ResourceOwnerPasswordResourceDetails resourceDetails) {
        return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resourceDetails);
    }
}