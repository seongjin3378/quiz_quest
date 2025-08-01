package com.seong.portfolio.quiz_quest.problems.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.comments.dto.CommentsDTO;
import com.seong.portfolio.quiz_quest.problems.enums.ProblemType;
import com.seong.portfolio.quiz_quest.visual.service.VisualService;
import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.service.problem.ProblemService;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import com.seong.portfolio.quiz_quest.rankings.service.redis.RedisRankingService;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingKeyEnumVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RedisRankingVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import com.seong.portfolio.quiz_quest.utils.pagination.service.PaginationService;
import com.seong.portfolio.quiz_quest.utils.pagination.utils.PaginationUtil;
import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class  ProblemsController {
    private final ProblemRepository problemRepository;
    private final PaginationService paginationService;
    private final SessionService sessionService;
    private final RedisRankingService redisRankingService;
    private final ProblemService problemService;
    private final VisualService visualService;

    @GetMapping("/p/n/{index}")
    @Transactional
    public String solvePageV(@PathVariable long index, Model model, HttpSession session, HttpServletRequest req) throws JsonProcessingException {
        String userId = sessionService.getSessionId();
       /* 정책 바뀜
       RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();*/
        /*int userUsageTime = rankingService.findRankingScore(vo);
        * */

        findRankingScoreAndAddModelByUserId(userId, model);

        findProblemVOAndAddModelByIndex(index, model);

        List<CommentsDTO> commentsDTO = findProblemCommentsVOAndAddModelByIndex(index, model);

        findCursorAndAddModelByProblemCommentsVO(commentsDTO, model);

        findProblemVisualsAndAddModelByIndex(index, model, req);

        session.setAttribute("problemIndex", Long.toString(index));
        return "solving";
    }



    private void findRankingScoreAndAddModelByUserId(String userId, Model model) throws JsonProcessingException {
        String key = RankingKeyEnumVO.fromString("usage_time").getRankingKey().getKey()+":week";
        RedisRankingVO vo = RedisRankingVO.builder().key(key).value(userId).build();
        int userUsageTime = redisRankingService.findRankingScore(vo);
        log.info("user usage time is {}", userUsageTime);
        model.addAttribute("userUsageTime", userUsageTime);
    }

    private void findProblemVOAndAddModelByIndex(long index, Model model) {
        ProblemVO problemVO = problemService.findByProblemIdAndReplace(index);
        model.addAttribute("problem", problemVO);
    }

    private List<CommentsDTO> findProblemCommentsVOAndAddModelByIndex(long index, Model model) {
        List<CommentsDTO> commentsDTO = problemService.findAllProblemComments(index);
        model.addAttribute("problemComments", commentsDTO);
        return commentsDTO;
    }

    private void findCursorAndAddModelByProblemCommentsVO(List<CommentsDTO> commentsDTO, Model model) {
        String cursor = problemService.findCursor(commentsDTO);
        log.info("cursor: {}", cursor);
        model.addAttribute("cursor", cursor);
    }

    private void findProblemVisualsAndAddModelByIndex(long index, Model model, HttpServletRequest request) {
        List<VisualDTO> result = visualService.findAllVisual(index, "problem"); //problemId
        log.info("result: {}", result);
        List<VisualDTO> tableResult = result.stream()
                .filter(vo -> !Objects.isNull(vo.getVisualTables()))
                .collect(Collectors.toList());

        List<VisualDTO> pictures = result.stream()
                .filter(vo -> Objects.isNull(vo.getVisualTables()))
                .toList();


        log.info(pictures.toString());


        model.addAttribute("pictures", pictures);
        model.addAttribute("visualTables", tableResult);
        model.addAttribute("request", request);


    }

    @GetMapping("/p/pic/{visualId}/{boardType}")
    public ResponseEntity<Resource> problemPictureV(@PathVariable long visualId, @PathVariable String boardType) throws IOException {
        Resource img = visualService.loadAsResource(visualId, boardType);

        MediaType mediaType = MediaTypeFactory.getMediaType(img).orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(CacheControl
                .maxAge(1, TimeUnit.HOURS))
                .body(img); // 리소스 1시간 재사용 가능
    }



    @GetMapping("/p/{index}/s/{sortType}")
    public String problemsPageV(Model model, @PathVariable int index, @PathVariable int sortType, HttpServletResponse response) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        PaginationUtil.handlePagination(response, model, paginationService, new PaginationVO.Builder<ProblemRepository, String>()
                .index(index)
                .repository(problemRepository)
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
