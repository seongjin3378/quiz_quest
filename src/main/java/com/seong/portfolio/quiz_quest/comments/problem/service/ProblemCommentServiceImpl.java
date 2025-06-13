package com.seong.portfolio.quiz_quest.comments.problem.service;

import com.seong.portfolio.quiz_quest.comments.problem.repo.ProblemCommentsRepository;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import com.seong.portfolio.quiz_quest.comments.service.CommentService;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier("ProblemCommentService")
public class ProblemCommentServiceImpl implements CommentService {
    private final ProblemCommentsRepository problemCommentsRepository;
    private final SessionService sessionService;

    private int getCommentIdOrDefault(ProblemCommentsVO problemCommentsVO) {
        return Math.toIntExact(Optional.ofNullable(problemCommentsVO)
                .map(ProblemCommentsVO::getCommentId)
                .orElse((long) -1));
    }

    @Override
    @Transactional
    public List<Object> saveAndReturnComments(Object vo, String sortType) {
        ProblemCommentsVO problemCommentsVO = (ProblemCommentsVO) vo;
        problemCommentsVO.setAuthor(sessionService.getSessionId());
        long largestCommentId = getCommentIdOrDefault(problemCommentsVO); // largestCommentId가 null일 경우 -1 반환
        log.info("getCommentId: {}", problemCommentsVO.getCommentId());
        problemCommentsRepository.save(problemCommentsVO); // save 하는 과정에서 생성된 commentId를 problemCommentsVO에 자동으로 할당됨
        List<ProblemCommentsVO> result;
        boolean isNotReplyCommentReq = problemCommentsVO.getParentCommentId() == null;

        if(sortType.equals("DESC")||isNotReplyCommentReq) { // 댓글이 아닌 답글 작성 요청이거나 최신순 정렬일 경우
            result = problemCommentsRepository.findAllByCommentIdAndProblemId(problemCommentsVO, largestCommentId);
            return Collections.singletonList(result);
        }

        return List.of();
    }

    @Override
    public List<Object> findComments(long id, String sortType, String cursor) {
        return Collections.singletonList(problemCommentsRepository.findAllByProblemIdAndSortTypeAndCursor(id, sortType, cursor));
    }

    @Override
    public List<Object> findAllReplyComments(long parentCommentId, String sortType, String cursor) {

        if(cursor.equals("0")) {
            return Collections.singletonList(problemCommentsRepository
                    .findAllByParentCommentId(parentCommentId, sortType, "0"));

        }else{
            return Collections.singletonList(problemCommentsRepository
                    .findAllByParentCommentId(parentCommentId, sortType, cursor));
        }
    }
}
