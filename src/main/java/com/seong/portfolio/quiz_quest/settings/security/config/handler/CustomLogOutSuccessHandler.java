package com.seong.portfolio.quiz_quest.settings.security.config.handler;

import com.seong.portfolio.quiz_quest.user.service.SessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogOutSuccessHandler implements LogoutSuccessHandler {

    private final SessionRegistry sessionRegistry;
    private final SessionService sessionService;
    Logger logger = LoggerFactory.getLogger(CustomLogOutSuccessHandler.class);
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername(); // 사용자 ID (또는 다른 식별자)
        logger.info("userId: {}", userId);
        sessionRegistry.removeSessionInformation(userId);

        response.sendRedirect("/login"); // 로그아웃 후 리다이렉트할 URL
    }
}
