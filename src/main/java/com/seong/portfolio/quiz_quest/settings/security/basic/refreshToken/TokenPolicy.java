package com.seong.portfolio.quiz_quest.settings.security.basic.refreshToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.util.List;

/*
* getRefreshToken()
* - Oauth2.0 인증을 받은 객체를 불러옴
* - 인증된 클라이언트Id를 가져옴 ex) google, github
* - 인증 등록 Id와 클라이언트 Id를 이용하여 인증 정보 반환
* - 인증 정보 반환 객체에 refreshToken 객체를 최종적으로 반환
* */

/*
* getCookieAndTokenValue()
* - refreshToken 객체에 값이 있을 경우 - google
*   - String type 변수에 refreshToken 값을 넣어 최종적으로 List형태로 cookie와 String Type token 값을 반환
* - refreshToken 객체 값이 없을 경우 - github
*   - String type 변수에 UUID 값을 넣어 마찬가지로 List로 반환
* github는 accessToken
* */



public interface TokenPolicy<T> {
    OAuth2RefreshToken getRefreshToken(Authentication authentication);
    List<?> getCookieAndTokenValue(T token);
    void validateRefreshToken(OAuth2RefreshToken refreshToken);
}
