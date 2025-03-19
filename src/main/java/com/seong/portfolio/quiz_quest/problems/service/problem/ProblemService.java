package com.seong.portfolio.quiz_quest.problems.service.problem;

import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;

import java.util.List;

public interface ProblemService {
    ProblemVO findProblem(long id);
    List<ProblemCommentsVO> findAllProblemComments(long id);
    String findCursor(List<ProblemCommentsVO> problemCommentsVO);
}
