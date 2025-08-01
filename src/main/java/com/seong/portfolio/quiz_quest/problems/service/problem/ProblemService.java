package com.seong.portfolio.quiz_quest.problems.service.problem;

import com.seong.portfolio.quiz_quest.comments.dto.CommentsDTO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;

import java.util.List;

public interface ProblemService {
    ProblemVO findByProblemIdAndReplace(long id);
    List<CommentsDTO> findAllProblemComments(long id);
    String findCursor(List<CommentsDTO> commentsDTO);
    ProblemVO findByProblemId(long id);
}
