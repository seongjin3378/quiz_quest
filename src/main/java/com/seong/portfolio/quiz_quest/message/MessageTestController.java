package com.seong.portfolio.quiz_quest.message;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageTestController {
    @GetMapping("/test")
    public String testV()
    {
        return "test";
    }
}
