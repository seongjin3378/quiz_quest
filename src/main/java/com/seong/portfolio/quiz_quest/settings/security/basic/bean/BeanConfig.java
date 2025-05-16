package com.seong.portfolio.quiz_quest.settings.security.basic.bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class BeanConfig {
    private final OAuth2ClientProperties properties;
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                 OAuth2AuthorizedClientRepository authorizedClientRepository) {
        return new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
    }
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration() , this.gitHubClientRegistration());
    }
    private ClientRegistration googleClientRegistration() {
        OAuth2ClientProperties.Registration googleRegistration = this.properties.getRegistration().get("google");

        log.info(googleRegistration.toString());
        log.info("Google Client ID: {}", googleRegistration.getClientId());
        log.info("Google Client Secret: {}", googleRegistration.getClientSecret());
        log.info("Google Scope: {}", googleRegistration.getScope());
        log.info("Google Redirect URI: {}", googleRegistration.getRedirectUri());
        log.info("Google Authorization Grant Type: {}", googleRegistration.getAuthorizationGrantType());
        return ClientRegistration.withRegistrationId("google")
                .clientId(googleRegistration.getClientId())
                .clientSecret(googleRegistration.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(googleRegistration.getRedirectUri())
                .scope(googleRegistration.getScope())
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }
    private ClientRegistration gitHubClientRegistration() {
        OAuth2ClientProperties.Registration githubRegistration = this.properties.getRegistration().get("github");

        log.info(githubRegistration.toString());
        log.info("Github Client ID: {}", githubRegistration.getClientId());
        log.info("Github Client Secret: {}", githubRegistration.getClientSecret());
        log.info("Github Scope: {}", githubRegistration.getScope());
        log.info("Github Redirect URI: {}", githubRegistration.getRedirectUri());
        log.info("Github Authorization Grant Type: {}", githubRegistration.getAuthorizationGrantType());
        return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId(githubRegistration.getClientId()).clientSecret(githubRegistration.getClientSecret()).build();/*ClientRegistration.withRegistrationId("github")
                .clientId(githubRegistration.getClientId())
                .clientSecret(githubRegistration.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(githubRegistration.getRedirectUri())
                .scope(githubRegistration.getScope())
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .clientName("Github")
                .build();*/
    }

}
