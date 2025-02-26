package com.seong.portfolio.quiz_quest.admin.problems.controller;


import com.seong.portfolio.quiz_quest.problems.enums.ProblemType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminProblemController {

    @GetMapping("/p/editor")
    public String editorV(Model model) {
        List<String> problemTypeList = new ArrayList<>();

        for(ProblemType problemType : ProblemType.values()) {
            problemTypeList.add(problemType.getDisplayName());
            log.info("problemType: {}", problemType.getDisplayName());
        }
        model.addAttribute("problemTypeList", problemTypeList);
        return "problemEditor";
    }
}
