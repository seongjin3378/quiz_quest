package com.seong.portfolio.quiz_quest.settings.security.config.handler;

import com.seong.portfolio.quiz_quest.settings.security.config.refreshToken.TokenPolicy;
import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler  implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenPolicy<OAuth2RefreshToken> tokenPolicy;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        OAuth2RefreshToken refreshToken = tokenPolicy.getRefreshToken(authentication);
        List<?> result = tokenPolicy.getCookieAndTokenValue(refreshToken);
        Cookie resultCookie = (Cookie) result.get(0);
        String tokenValue= (String) result.get(1);
        response.addCookie(resultCookie); // 응답에 쿠키 추가
        userRepository.updateRefreshTokenByUserId(authentication.getName(), tokenValue);
        //log.info("Refresh Token: {}", refreshToken.getTokenValue());
        response.sendRedirect("/");
        }



}

