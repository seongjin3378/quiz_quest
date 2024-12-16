package com.seong.portfolio.quiz_quest.user.controller;


import com.seong.portfolio.quiz_quest.user.service.UserService;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class JoinController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/join")
    public String join() {
        return "join";
    }
    @PostMapping("/joinProc")
    public String joinProc(UserVO vo) {
        vo.setPassword(bCryptPasswordEncoder.encode(vo.getPassword()));
        vo.setRole("ROLE_USER");
        userService.joinProcess(vo);

        return "/login";
    }


}
