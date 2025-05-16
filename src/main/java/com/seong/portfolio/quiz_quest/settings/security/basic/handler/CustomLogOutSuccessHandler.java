package com.seong.portfolio.quiz_quest.settings.security.basic.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogOutSuccessHandler implements LogoutSuccessHandler {

    private final SessionRegistry sessionRegistry;
    Logger logger = LoggerFactory.getLogger(CustomLogOutSuccessHandler.class);
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername(); // 사용자 ID (또는 다른 식별자)
        logger.info("remove userId: {}", userId);
        sessionRegistry.removeSessionInformation(userId);
        SecurityContextHolder.getContext().setAuthentication(null); //인증 권한을 해제
        expireCookie(request, response);
        response.sendRedirect("/login"); // 로그인 페이지로 이동
    }


    private void expireCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // 쿠키 삭제
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setPath("/"); // 쿠키의 경로 설정
        cookie.setMaxAge(0); // 쿠키 만료 설정
        response.addCookie(cookie);
        logger.info("쿠키 삭제");
    }

    



}
