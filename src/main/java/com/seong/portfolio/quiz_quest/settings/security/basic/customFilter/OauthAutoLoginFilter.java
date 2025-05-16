package com.seong.portfolio.quiz_quest.settings.security.basic.customFilter;

import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.service.CustomUserDetailsService;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.vo.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
@Component
public class OauthAutoLoginFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            String refreshToken = getRefreshTokenFromCookies(request.getCookies());
            log.info("Refresh token: {}", refreshToken);
            String userId = userRepository.findUserIdByRefreshToken(refreshToken);
            log.info("User ID: {}", userId);
            if (userId != null) {
                PrincipalDetails principalDetails = customUserDetailsService.loadUserByUsername(userId);
                Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(auth);
            }

        }
        filterChain.doFilter(request, response);
    }
}
