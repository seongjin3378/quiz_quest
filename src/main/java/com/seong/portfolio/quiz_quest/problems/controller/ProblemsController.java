package com.seong.portfolio.quiz_quest.problems.controller;


import com.seong.portfolio.quiz_quest.comments.problem.repo.ProblemCommentsRepository;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import com.seong.portfolio.quiz_quest.problems.enums.ProblemType;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import com.seong.portfolio.quiz_quest.rankings.service.RankingService;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import com.seong.portfolio.quiz_quest.user.service.UserService;
import com.seong.portfolio.quiz_quest.utils.pagination.service.PaginationService;
import com.seong.portfolio.quiz_quest.utils.pagination.utils.PaginationUtil;
import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProblemsController {
    private final ProblemRepository problemRepository;
    private final PaginationService paginationService;
    private final ProblemCommentsRepository problemCommentsRepository;
    private final SessionService sessionService;
    private final RankingService rankingService;
    private final UserService userService;

    @GetMapping("/p/n/{index}")
    public String solvePageV(@PathVariable long index, Model model, HttpSession session)
    {
        String userId = sessionService.getSessionId();
        RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();

        int userUsageTime = rankingService.findRankingScore(vo);


        model.addAttribute("userUsageTime", userUsageTime);

        ProblemVO problemVO = problemRepository.findByProblemId(ProblemVO.builder().problemId(index).isVisible(1).build());
        List<ProblemCommentsVO> problemCommentsVO = problemCommentsRepository.findAllByProblemId(index, "DESC", null);
        int lastIndex = !problemCommentsVO.isEmpty() ? problemCommentsVO.size() - 1 : 0;
        String cursor = lastIndex != 0 ? problemCommentsVO.get(lastIndex).getCursor() : "0";
        model.addAttribute("problem", problemVO);
        model.addAttribute("problemComments", problemCommentsVO);
        log.info("cursor: {}", cursor);
        model.addAttribute("cursor", cursor);
        session.setAttribute("problemIndex", Long.toString(index));
        return "solving";
    }

    @GetMapping("/p/{index}/s/{sortType}")
    public String problemsPageV(Model model, @PathVariable int index, @PathVariable int sortType, HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        PaginationUtil.handlePagination(problemRepository, response, model, paginationService, new PaginationVO.Builder<>()
                .index(index)
                .sortType(sortType)
                .column("problem_type")
                .value(ProblemType.getDisplayNameByIndex(sortType))
                .valueOfOnePage(10)
                .url("/p/s")
                .pageItemCount(10)
                .build());

        return "problems";
    }
}
