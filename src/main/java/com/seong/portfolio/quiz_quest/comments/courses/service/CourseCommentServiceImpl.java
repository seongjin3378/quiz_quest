package com.seong.portfolio.quiz_quest.comments.courses.service;

import com.seong.portfolio.quiz_quest.comments.courses.repo.CourseCommentsRepository;
import com.seong.portfolio.quiz_quest.comments.courses.vo.CourseCommentsVO;
import com.seong.portfolio.quiz_quest.comments.service.CommentService;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Deprecated
public class CourseCommentServiceImpl  {

    private final SessionService sessionService;
    private final CourseCommentsRepository courseCommentsRepository;
    private int getCommentIdOrDefault(CourseCommentsVO courseCommentsVO) {
        return Math.toIntExact(Optional.ofNullable(courseCommentsVO)
                .map(CourseCommentsVO::getCommentId)
                .orElse((long) -1));
    }


    public List<Object> saveAndReturnComments(Object vo, String sortType) {
        CourseCommentsVO courseCommentsVO = (CourseCommentsVO) vo;
        long largestCommentId = getCommentIdOrDefault(courseCommentsVO); // largestCommentId가 null일 경우 -1 반환
        log.info("getCommentId: {}", courseCommentsVO.getCommentId());
        courseCommentsRepository.save(courseCommentsVO); // save 하는 과정에서 생성된 commentId를 courseCommentsVO에 자동으로 할당됨
        List<CourseCommentsVO> result;
        boolean isNotReplyCommentReq = courseCommentsVO.getParentCommentId() == null;

        if(sortType.equals("DESC")||isNotReplyCommentReq) { // 댓글이 아닌 답글 작성 요청이거나 최신순 정렬일 경우
            result = courseCommentsRepository.findAllByCommentIdAndCourseId(courseCommentsVO, largestCommentId);
            return Collections.singletonList(result);
        }

        return List.of();
    }

    public List<Object> findComments(long id, String sortType, String cursor, String boardType) {
        return Collections.singletonList(courseCommentsRepository.findAllByCourseIdAndAndSortTypeAndCursor(id, sortType, cursor));
    }


    public List<Object> findAllReplyComments(long parentCommentId, String sortType, String cursor) {
        if(cursor.equals("0")) {
            return Collections.singletonList(courseCommentsRepository.findAllByParentCommentId(parentCommentId, sortType, "0"));

        }else{
            return Collections.singletonList(courseCommentsRepository.findAllByParentCommentId(parentCommentId, sortType, cursor));
        }
    }
}
