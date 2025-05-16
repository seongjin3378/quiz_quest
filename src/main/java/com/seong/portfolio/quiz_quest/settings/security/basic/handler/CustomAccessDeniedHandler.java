package com.seong.portfolio.quiz_quest.settings.security.basic.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
/* AccessDeniedHandler
  - 사용자가 인증되었지만 접근 권한이 없는 리소스에 접근할 때 호출
*/
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if(request.getRequestURI().equals("/login"))
        {
           response.sendRedirect("/");
        }
    }
}
