package com.seong.portfolio.quiz_quest.problems.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.comments.problem.repo.ProblemCommentsRepository;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import com.seong.portfolio.quiz_quest.problems.enums.ProblemType;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.service.problem.ProblemService;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import com.seong.portfolio.quiz_quest.rankings.service.ranking.RankingService;
import com.seong.portfolio.quiz_quest.rankings.service.redis.RedisRankingService;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingKeyEnumVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RedisRankingVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import com.seong.portfolio.quiz_quest.utils.pagination.service.PaginationService;
import com.seong.portfolio.quiz_quest.utils.pagination.utils.PaginationUtil;
import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class  ProblemsController {
    private final ProblemRepository problemRepository;
    private final PaginationService paginationService;
    private final ProblemCommentsRepository problemCommentsRepository;
    private final SessionService sessionService;
    private final RankingService rankingService;
    private final RedisRankingService redisRankingService;
    private final ProblemService problemService;

    @GetMapping("/p/n/{index}")
    @Transactional
    public String solvePageV(@PathVariable long index, Model model, HttpSession session) throws JsonProcessingException {
        String userId = sessionService.getSessionId();
       /* 정책 바뀜
       RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();*/
        /*int userUsageTime = rankingService.findRankingScore(vo);
        * */
        String key = RankingKeyEnumVO.fromString("usage_time").getRankingKey().getKey()+":week";
        RedisRankingVO vo = RedisRankingVO.builder().key(key).value(userId).build();
        int userUsageTime = redisRankingService.findRankingScore(vo);
        log.info("user usage time is {}", userUsageTime);

        model.addAttribute("userUsageTime", userUsageTime);

        ProblemVO problemVO = problemService.findProblem(index);
        List<ProblemCommentsVO> problemCommentsVO = problemService.findAllProblemComments(index);
        String cursor = problemService.findCursor(problemCommentsVO);

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
