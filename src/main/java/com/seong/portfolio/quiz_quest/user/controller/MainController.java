package com.seong.portfolio.quiz_quest.user.controller;


import com.seong.portfolio.quiz_quest.rankings.service.RankingService;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import com.seong.portfolio.quiz_quest.user.service.UserService;
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
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final SessionService sessionService;
    private final RankingService rankingService;
    private final UserService userService;
    @GetMapping("/")
    public String mainV(Model model)
    {
        String userId = sessionService.getSessionId();
        RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();

            int userUsageTime = rankingService.findRankingScore(vo);
            int userCount = userService.getAllActiveUsers();
            model.addAttribute("userId", userId);
            model.addAttribute("userUsageTime", userUsageTime);
            model.addAttribute("userCount", userCount);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info(authentication.getName());


        return "/main";
    }





}
