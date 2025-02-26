package com.seong.portfolio.quiz_quest.comments.problem.service;

import com.seong.portfolio.quiz_quest.comments.problem.repo.ProblemCommentsRepository;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemCommentServiceImpl implements ProblemCommentService {
    private final ProblemCommentsRepository problemCommentsRepository;
    private final SessionService sessionService;

    private int getCommentIdOrDefault(ProblemCommentsVO problemCommentsVO) {
        return Math.toIntExact(Optional.ofNullable(problemCommentsVO)
                .map(ProblemCommentsVO::getCommentId)
                .orElse((long) -1));
    }

    @Override
    @Transactional
    public List<ProblemCommentsVO> saveAndReturnProblemComments(ProblemCommentsVO problemCommentsVO, String sortType) {
        problemCommentsVO.setAuthor(sessionService.getSessionId());
        long largestCommentId = getCommentIdOrDefault(problemCommentsVO); // largestCommentId가 null일 경우 -1 반환
        log.info("getCommentId: {}", problemCommentsVO.getCommentId());
        problemCommentsRepository.save(problemCommentsVO); // save 하는 과정에서 생성된 commentId를 problemCommentsVO에 자동으로 할당됨
        List<ProblemCommentsVO> result;
        boolean isNotReplyCommentReq = problemCommentsVO.getParentCommentId() == null;

        if(sortType.equals("DESC")||isNotReplyCommentReq) { // 댓글이 아닌 답글 작성 요청이거나 최신순 정렬일 경우
            result = problemCommentsRepository.findAllByCommentIdAndProblemId(problemCommentsVO, largestCommentId);
            return result;
        }

        return List.of();
    }
}
