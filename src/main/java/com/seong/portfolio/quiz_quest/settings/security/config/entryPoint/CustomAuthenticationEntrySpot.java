package com.seong.portfolio.quiz_quest.settings.security.config.entryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/* AuthenticationEntryPoint
  - 인증되지 않은 사용자에게 어디에서 인증을 수행해야 하는지 알려주는 역할
  - 주로 로그인 페이지로 리다이렉트하거나 인증을 위한 다른 경로 제공
  - 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 호출
  - 예로, 로그인되지 않은 상태에서 보호된 페이지에 접근면 AuthenticationEntryPoint가 실행됨
 */
public class CustomAuthenticationEntrySpot implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntrySpot.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


        logger.info("CustomAuthenticationEntrySpot commence {}, {}", request.getRequestURI(), authException.getMessage());
    }
}
