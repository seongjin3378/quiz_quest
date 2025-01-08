package com.seong.portfolio.quiz_quest.problems.controller;


import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProblemsController {
    private final ProblemRepository problemRepository;

    @GetMapping("/p/{index}")
    public String solvePageV(@PathVariable long index, Model model, HttpSession session)
    {
        ProblemVO problemVO = problemRepository.findByProblemId(ProblemVO.builder().problemId(index).isVisible(1).build());
        model.addAttribute("problem", problemVO);
        session.setAttribute("problemIndex", Long.toString(index));
        return "solving";
    }

    @GetMapping("/p")
    public String problemsPageV(Model model)
    {
        List<ProblemVO> problemsList = problemRepository.findAll();
        model.addAttribute("problemsList", problemsList);
        return "problems";
    }
}
