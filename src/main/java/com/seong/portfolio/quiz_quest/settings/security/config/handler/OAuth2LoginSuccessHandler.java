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
       /* OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient user = oAuth2AuthorizedClientService.loadAuthorizedClient(registrationId, authentication.getName());

        OAuth2RefreshToken refreshToken = user.getRefreshToken();
        log.info("Authentication getName {}", authentication.getName());
        log.info("Authentication accessToken {}", user.getAccessToken().getTokenValue());
        assert refreshToken != null;
        List<?> getRefreshTokenArray = getRefreshTokenCookie(refreshToken);
        Cookie refreshTokenCookie = (Cookie) getRefreshTokenArray.get(0);
        String refreshTokenValue = (String) getRefreshTokenArray.get(1);*/

        OAuth2RefreshToken refreshToken = tokenPolicy.getRefreshToken(authentication);
        List<?> result = tokenPolicy.getCookieAndTokenValue(refreshToken);
        Cookie resultCookie = (Cookie) result.get(0);
        String tokenValue= (String) result.get(1);
        response.addCookie(resultCookie); // 응답에 쿠키 추가
        userRepository.updateRefreshTokenByUserId(authentication.getName(), tokenValue);
        //log.info("Refresh Token: {}", refreshToken.getTokenValue());
        response.sendRedirect("/");
        }

    /*private static List<?> getRefreshTokenCookie(OAuth2RefreshToken refreshToken) {
        String refreshTokenValue = "";
        if(refreshToken != null) {
            refreshTokenValue = refreshToken.getTokenValue();
        }else{
            refreshTokenValue = UUID.randomUUID().toString();
        }
        // Refresh Token을 쿠키에 추가

        Cookie refreshTokenCookie = new Cookie("refresh_token",  refreshTokenValue);
        refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가능하도록 설정
        refreshTokenCookie.setPath("/"); // 쿠키의 유효 경로 설정
        refreshTokenCookie.setMaxAge(3600 * 24 * 90); // 쿠키의 만료 시간 설정 (예: 90일)
        List<Object> result = new ArrayList<>();
        result.add(refreshTokenCookie);
        result.add(refreshTokenValue);

        return result;
    }*/


}

