package com.seong.portfolio.quiz_quest.user.controller;


import com.seong.portfolio.quiz_quest.rankings.service.RankingService;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import com.seong.portfolio.quiz_quest.user.service.user.UserService;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class JoinController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionService sessionService;
    private final RankingService rankingService;
    @GetMapping("/join")
    public String join() {
        return "join";
    }


    @PostMapping("/joinProc")
    @Transactional
    public String joinProc(UserVO vo) {
        vo.setPassword(bCryptPasswordEncoder.encode(vo.getPassword()));
        vo.setRole("ROLE_USER");
        vo.setXp(0);
        userService.joinProcess(vo);
        rankingService.initializeRankingDB(vo);
        return "/login";
    }


}
