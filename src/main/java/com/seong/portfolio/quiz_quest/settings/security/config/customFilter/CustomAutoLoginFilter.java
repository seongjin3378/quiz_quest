package com.seong.portfolio.quiz_quest.settings.security.config.customFilter;

import com.seong.portfolio.quiz_quest.user.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class CustomAutoLoginFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(CustomAutoLoginFilter.class);
    private final SessionService sessionService;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String userId = sessionService.getSessionId();
        logger.info("userId: {}", userId);
        String key = "JSESSIONID:" + userId;

        /*
        if(userId.equals("Anonymous"))
        {
            response.sendRedirect("/login");
        }

         */

        if(!userId.equals("Anonymous") && redisTemplate.opsForValue().get(key) == null ) {

            redisTemplate.opsForValue().set(key, userId);
            logger.info("세션 생성: {}, Redis에 저장된 활성 세션 수: {}", userId, redisTemplate.opsForValue().get(key));
        }

        /*
        if(cookies != null && !userExists) // 쿠키들이 있고, 세션 레지스트리에 유저가 없을 경우
        {
            for (Cookie cookie: cookies){
                if("remember-me".equals(cookie.getName()))
                {
                    logger.info("authentication: {}", userDetails);
                    if(userDetails != null)
                    {
                        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        sessionRegistry.registerNewSession(userId, userDetails); // sessionRegistry에 principal 등록
                    }
                    break;
                }
            }
        }
        *
         */
        filterChain.doFilter(request, response);


    }
}


