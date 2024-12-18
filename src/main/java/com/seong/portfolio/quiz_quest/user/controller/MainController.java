package com.seong.portfolio.quiz_quest.user.controller;


import com.seong.portfolio.quiz_quest.ranking.service.RankingService;
import com.seong.portfolio.quiz_quest.ranking.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final SessionService sessionService;
    private final RankingService rankingService;
    @GetMapping("/")
    public String mainV(Model model)
    {
        String userId = sessionService.getSessionId();
        RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();

            int userUsageTime = rankingService.findRankingScore(vo);
            model.addAttribute("userId", userId);
            model.addAttribute("userUsageTime", userUsageTime);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info(authentication.getName());


        return "/main";
    }




}
