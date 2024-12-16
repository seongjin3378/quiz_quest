package com.seong.portfolio.quiz_quest.user.controller;


import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class LoginController {
    @GetMapping("/login")
    public String loginV()
    {
        return "login";
    }

    @GetMapping("/admin")
    public String adminV() {
        return "admin";
    }



}
