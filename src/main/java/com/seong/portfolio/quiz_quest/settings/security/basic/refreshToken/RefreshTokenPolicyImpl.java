package com.seong.portfolio.quiz_quest.settings.security.basic.refreshToken;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenPolicyImpl implements TokenPolicy<OAuth2RefreshToken> {
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    @Override
    public OAuth2RefreshToken getRefreshToken(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient user = oAuth2AuthorizedClientService.loadAuthorizedClient(registrationId, authentication.getName());
        log.info("Authentication accessToken {}", user.getAccessToken().getTokenValue());
        return user.getRefreshToken();
    }

    @Override
    public List<?> getCookieAndTokenValue(OAuth2RefreshToken refreshToken) {
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
    }

    @Override
    public void validateRefreshToken(OAuth2RefreshToken refreshToken) {

    }
}
