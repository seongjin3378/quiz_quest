package com.seong.portfolio.quiz_quest.settings.security.config.entryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mysql.cj.conf.ConnectionUrlParser.parseUserInfo;

/* AuthenticationEntryPoint
  - 인증되지 않은 사용자에게 어디에서 인증을 수행해야 하는지 알려주는 역할
  - 주로 로그인 페이지로 리다이렉트하거나 인증을 위한 다른 경로 제공
  - 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 호출
  - 예로, 로그인되지 않은 상태에서 보호된 페이지에 접근면 AuthenticationEntryPoint가 실행됨
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntrySpot implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntrySpot.class);
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    private String getRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // 쿠키가 없으면 null 반환
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


        logger.info("CustomAuthenticationEntrySpot commence {}, {}", request.getRequestURI(), authException.getMessage());
        if (request.getRequestURI().equals("/login")) {
            response.sendRedirect("/");
        } else if (request.getRequestURI().equals("/")) {
/*            // Refresh Token을 쿠키에서 가져오기
            String refreshTokenValue = getRefreshTokenFromCookies(request.getCookies());

            if (refreshTokenValue != null) {
                response.sendRedirect("/oauth2/authorize/google");
                return;
            }*/
            response.sendRedirect("/login");
            }

        }
    }

